package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
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
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_CATEGORY;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_TAG;

public class FragmentSearchResults extends Fragment implements IFragmentSearchNewsResult {
    WindowSearchResultsBinding binding;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;

    //retrofit call
    Retrofit retrofit;
    Call<JsonObject> searchNews = null;
    ParserSimpleNewsModel parserSimpleNewsModel;

    //Variables
    LinearLayoutManager manager;
    MediumNewsAdapter adapter;
    int offset = 1;
    final String limit = "10";

    //Search Variables
    int searchWhere;
    private String searchingID;

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

        //TODO: Input arguments
        searchWhere = getArguments().getInt(KEY_SEARCH_WHERE, -1);
        searchingID = getArguments().getString(KEY_SEARCH_ID);

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
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.toolbar.setTitle(
                getArguments().getString(KEY_SEARCH_TOOLBAR_NAME)
        );


        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new MediumNewsAdapter(getContext(), new ArrayList<>());
        binding.listSearchResult.setLayoutManager(manager);
        binding.listSearchResult.setAdapter(adapter);
        binding.listSearchResult.setHasFixedSize(true);

        bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listSearchResult, false);
        bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listSearchResult, false);


        //initiate list scrolling state change listener
        binding.listSearchResult.addOnScrollListener(scrollListener);

        //starting
        search_text(true);
    }

    //TODO: Networking:

    //search news with title
    void search_text(boolean fromBegin) {

        if (fromBegin) {
            offset = 1;
            binding.progress.setVisibility(View.VISIBLE);
        }

        if (searchNews != null) {
            searchNews.cancel();
        }

        if (searchWhere == SEARCH_CATEGORY) {
            Log.d(DELETE_LOG, "in search category");
            searchNews = retrofit
                    .create(MyRestService.class)
                    .getNewsWithCategory(String.valueOf(offset),
                            limit,
                            searchingID,
                            MySettings.getInstance().getLang());
        } else if (searchWhere == SEARCH_TAG) {
            searchNews = retrofit
                    .create(MyRestService.class)
                    .getNewsWithTags(String.valueOf(offset),
                            limit,
                            searchingID,
                            MySettings.getInstance().getLang());
        }


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

                if (offset == 1)
                    adapter.withHeaderNoInternet(bindingNoConnection.getRoot());
                else
                    adapter.withFooter(bindingFooter.getRoot());
            }
        });
    }

    //####################################################################################

    //TODO: Parsing
    //parsing last news continue(pager news)
    private void parsingLastNewsContinue(JsonObject json) {
        parserSimpleNewsModel = new ParserSimpleNewsModel(this);

        //sending data to view
        if (offset == 1)
            initNews(parserSimpleNewsModel.parse(json));
        else
            addNews(parserSimpleNewsModel.parse(json));
    }

    //TODO: Interface override methods

    @Override
    public void initNews(List<NewsSimpleModel> items) {
        binding.progress.setVisibility(View.GONE);
        adapter.removeHeaders();
        adapter.init_items(items);
    }

    @Override
    public void addNews(List<NewsSimpleModel> items) {
        adapter.hideLoading();
        isLoading = false;
        adapter.add_all(items);
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

                adapter.removeFooter();
                adapter.showLoading();
                search_text(false);
            }
        }

    };

    //TODO: Override methods

    @Override
    public void onStop() {
        Log.d(CALLBACK_LOG, "SearchResult onStop");

        super.onStop();
        MyUtil.closeSoftKeyboard(getActivity());
    }


}