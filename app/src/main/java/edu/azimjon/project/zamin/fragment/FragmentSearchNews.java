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
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowSearchBinding;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.view.IFragmentSearchNews;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_ITEMS;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class FragmentSearchNews extends Fragment implements IFragmentSearchNews {
    WindowSearchBinding binding;
    FooterNoConnectionBinding bindingFooter;
    WindowNoConnectionBinding bindingNoConnection;
    View headerNoItem;

    //retrofit call
    Retrofit retrofit;
    Call<JsonObject> searchNews = null;


    //Variables
    LinearLayoutManager manager;
    MediumNewsAdapter adapter;
    int offset = 1;
    private String searchingText = "";

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
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //set that news content opened from search news
        MySettings.getInstance().setWhichIdCallsContent(R.id.fragmentSearchNews);
        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            adapter = new MediumNewsAdapter(getContext(), new ArrayList<>());
            binding.listSearchResult.setLayoutManager(manager);
            binding.listSearchResult.setAdapter(adapter);
            binding.listSearchResult.setHasFixedSize(true);
            binding.listSearchResult.setItemAnimator(null);

            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listSearchResult, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listSearchResult, false);
            headerNoItem = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.header_no_items,
                            binding.listSearchResult,
                            false);
        } else {
            reloadContent();
        }

        //initiate search Edit text Watcher
        binding.edSearch.addTextChangedListener(myTextWatcher);

        //initiate list scrolling state change listener
        binding.listSearchResult.addOnScrollListener(scrollListener);

        //TODO: changing locale
        binding.edSearch.setHint(MyUtil.getLocalizedString(getContext(), R.string.text_news));
        ((TextView) headerNoItem.findViewById(R.id.text_no_item)).setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_items));
    }

    private void reloadContent() {
        adapter.notifyDataSetChanged();
    }


    //TODO: Networking:

    //search news with title
    void search_text(boolean fromBegin) {
        if (TextUtils.isEmpty(searchingText.trim())) {
            return;
        }

        if (fromBegin) {
            adapter.clearItems();
            offset = 1;
            binding.progress.setVisibility(View.VISIBLE);
        }

        if (searchNews != null) {
            searchNews.cancel();
        }

        searchNews = retrofit
                .create(MyRestService.class)
                .searchNewsWithTitle(String.valueOf(offset),
                        "10",
                        searchingText.trim(),
                        MySettings.getInstance().getLang());

        adapter.removeHeaders();
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

                if (offset == 1) {
                    adapter.withHeaderNoInternet(bindingNoConnection.getRoot());
                } else
                    adapter.withFooter(bindingFooter.getRoot());
            }
        });
    }

    //####################################################################################

    //TODO: Parsing
    //parsing last news continue(pager news)
    private void parsingLastNewsContinue(JsonObject json) {
        if (offset == 1 && json.getAsJsonArray("articles").size() == 0) {
            initNews(null, MESSAGE_NO_ITEMS);
            return;
        }

        //sending data to view
        if (offset == 1)
            initNews(ParserSimpleNewsModel.parse(json, -1), MESSAGE_OK);
        else
            addNews(ParserSimpleNewsModel.parse(json, -1), MESSAGE_OK);

    }

    //TODO: Interface override methods

    @Override
    public void initNews(List<SimpleNewsModel> items, int message) {
        if (getContext() == null)
            return;

        if (message == MESSAGE_NO_ITEMS) {
            adapter.withHeaderNoItem(headerNoItem);
        } else if (message == MESSAGE_NO_CONNECTION) {
            adapter.withHeaderNoInternet(bindingNoConnection.getRoot());
        } else if (message == MESSAGE_OK) {
            adapter.removeHeaders();
            binding.progress.setVisibility(View.GONE);
            adapter.initItems(items);
        }

    }

    @Override
    public void addNews(List<SimpleNewsModel> items, int message) {
        if (getContext() == null)
            return;

        adapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_OK) {
            adapter.addItems(items);
        }

    }

    //####################################################################################


    //TODO: Argument variables

    TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s.toString().trim())) {
                if (!searchingText.equals(s.toString())) {
                    searchingText = s.toString();
                    search_text(true);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

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
//        binding.edSearch.setText("");
        MyUtil.closeSoftKeyboard(Objects.requireNonNull(getActivity()));
        super.onStop();
    }


}