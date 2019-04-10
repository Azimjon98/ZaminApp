package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_CATEGORY;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_TAG;

public class FragmentNewsContent extends Fragment implements IFragmentNewsContent {
    //TODO: Constants here


    //TODO: variables here
    LinearLayoutManager manager;
    WindowNewsContentBinding binding;
    HeaderWindowNewsContentBinding bindingHeader;
    PresenterNewsContent presenterNewsContent;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterNewsContent = new PresenterNewsContent(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_news_content, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack(R.id.fragmentContent, true));

        //initialize adapters and append to lists

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.listLastNews.setLayoutManager(manager);
        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());

        bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_content, binding.listLastNews, false);
        bindingHeader.getRoot().setPadding(0, 0, 0, MyUtil.dpToPx(24));
        mediumNewsAdapter.withHeader(bindingHeader.getRoot());
        binding.listLastNews.setAdapter(mediumNewsAdapter);

        binding.listLastNews.addOnScrollListener(scrollListener);


        //*****************************************************************************

        //TODO: Header binding initializators
        // Enable Javascript
        bindingHeader.contentWeb.getSettings().setJavaScriptEnabled(true);


        bindingHeader.contentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                bindingHeader.progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                injectCSS();
                super.onPageFinished(view, url);
                bindingHeader.progress.setVisibility(View.GONE);

            }
        });
        Log.d(Constants.MY_LOG, "in fragmentview:" + getArguments().getString(Constants.WEB_URL));

        initIcons();
        String newsId = getArguments().getString(KEY_NEWS_ID);
        presenterNewsContent.init(newsId);

    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initContent(NewsContentModel model) {
        bindingHeader.setModel(model);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        bindingHeader.contentWeb.loadUrl(model.getContent());
        Log.d(MY_LOG, "initContent: " + model.getContent());
    }

    @Override
    public void addLastNews(List<NewsSimpleModel> items) {
        mediumNewsAdapter.add_all(items);
        if (mediumNewsAdapter.getItemCount() > 1)
            mediumNewsAdapter.hideLoading();
        isLoading = false;
    }

    @Override
    public void initTags(List<String> tags) {
        tags.add("Манчестер Юнайтед");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String tag : tags) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_tag, null,false);
            chip.setText(tag);
//            chip.setChipBackgroundColorResource(R.color.icon_active);


            chip.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SEARCH_ID, tag);
                bundle.putString(KEY_SEARCH_TOOLBAR_NAME, tag);
                bundle.putInt(KEY_SEARCH_WHERE, SEARCH_TAG);
                Navigation.findNavController(v).navigate(R.id.action_global_fragmentSearchResults, bundle);
            });

            bindingHeader.tagsGroup.addView(chip);
        }

    }


    //#################################################################

    //TODO: Additional methods

    private void initIcons() {
        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.iconBookmark.setOnClickListener(v -> Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show());
    }

    //#################################################################

    //TODO: Argument variables

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
                mediumNewsAdapter.showLoading();
                presenterNewsContent.getContinue();
            }
        }

    };


}