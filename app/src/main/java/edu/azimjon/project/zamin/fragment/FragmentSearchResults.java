package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.WindowSearchBinding;
import edu.azimjon.project.zamin.databinding.WindowSearchResultsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.view.IFragmentSearchNewsResult;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.KEY_CATEGORY_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_CATEGORY_NAME;

public class FragmentSearchResults extends Fragment implements IFragmentSearchNewsResult {
    WindowSearchResultsBinding binding;

    //retrofit call
    Retrofit retrofit;
    Call<JsonObject> searchNews = null;
    ParserSimpleNewsModel parserSimpleNewsModel;

    //Variables
    LinearLayoutManager manager;
    MediumNewsAdapter adapter;
    int offset = 1;
    final String limit = "10";
    private String searchingText;

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;

    int total_items, visible_items, scrollout_items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_search_results, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Input arguments
        searchingText = getArguments().getString(KEY_CATEGORY_ID);
        binding.toolbar.setTitle(
                getArguments().getString(KEY_CATEGORY_NAME)
        );

        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MediumNewsAdapter(getContext(), new ArrayList<>());
        binding.listSearchResult.setLayoutManager(manager);
        binding.listSearchResult.setAdapter(adapter);


        //initiate list scrolling state change listener
        binding.listSearchResult.addOnScrollListener(scrollListener);

        //starting
        search_text(true);
    }

    //TODO: Networking:

    //search news with title
    void search_text(boolean fromBegin) {
        if (TextUtils.isEmpty(searchingText.trim())) {
            return;
        }

        if (fromBegin) {
            offset = 1;
            binding.progress.setVisibility(View.VISIBLE);
        }

        if (searchNews != null) {
            searchNews.cancel();
        }

        searchNews = retrofit
                .create(MyRestService.class)
                .getNewsWithCategory(String.valueOf(offset),
                        limit,
                        searchingText.trim(),
                        "uz");


        searchNews.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    parsingLastNewsContinue(json);

                    offset++;

                } else {
                    Log.d(API_LOG, "searchNews : error: " + response.message());
                }

                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d(API_LOG, "searchNews onFailure" + t.getMessage());
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    //####################################################################################

    //TODO: Parsing
    //parsing last news continue(pager news)
    private void parsingLastNewsContinue(JsonObject json) {
        if (parserSimpleNewsModel == null)
            parserSimpleNewsModel = new ParserSimpleNewsModel(this);

        //sending data to view
        initNews(parserSimpleNewsModel.parse(json));
    }

    //TODO: Interface override methods

    @Override
    public void initNews(List<NewsSimpleModel> items) {
        adapter.init_items(items);
    }

    @Override
    public void addNews(List<NewsSimpleModel> items) {
        adapter.hideLoading();
        adapter.add_items(items);

        isLoading = false;
    }

    //####################################################################################


    //TODO: Argument variables

    //load data continue when scrolling to the end
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            Log.d(Constants.MY_LOG, "onScrollStateChanged");

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            total_items = manager.getItemCount();
            visible_items = manager.getChildCount();
            scrollout_items = manager.findFirstVisibleItemPosition();
            Log.d(Constants.MY_LOG, "total_items: " + total_items + " " +
                    "visible_items: " + visible_items + " " +
                    "scrollout_items: " + scrollout_items);

            if (isScrolling && (visible_items + scrollout_items == total_items) && !isLoading) {
                isScrolling = false;
                isLoading = true;
                adapter.showLoading();
                search_text(false);
            }
        }

    };

    //TODO: Override methods

    @Override
    public void onStop() {
        super.onStop();
        MyUtil.closeSoftKeyboard(getActivity());
    }


}