package edu.azimjon.project.zamin.fragment;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_CATEGORY;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_TAG;
import static edu.azimjon.project.zamin.addition.Constants.WEB_URL;

public class FragmentNewsContent extends Fragment implements IFragmentNewsContent {
    //TODO: Constants here
    String newsId;
    NewsSimpleModel simpleModel;
    NewsContentModel model;

    //TODO: variables here
    LinearLayoutManager manager;
    WindowNewsContentBinding binding;
    HeaderWindowNewsContentBinding bindingHeader;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;

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
        newsId = getArguments().getString(KEY_NEWS_ID);
        simpleModel = getArguments().getParcelable(KEY_NEWS_MODEL);
        model = Converters.fromSimpleNewstoContentNews(simpleModel);

        presenterNewsContent = new PresenterNewsContent(this);
        MySettings.getInstance().increaseStackCount();
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
        binding.iconBack.setOnClickListener(v -> {
            new MyAsycTask(this.getActivity(), Navigation.findNavController(v)).execute();
        });

        //initialize adapters and append to lists

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.listLastNews.setLayoutManager(manager);
        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.getRoot().setPadding(0, 0, 0, MyUtil.dpToPx(24));

        bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_content, binding.listLastNews, false);
        bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listLastNews, false);
        bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listLastNews, false);

        bindingHeader.setModel(model);
        mediumNewsAdapter.withHeader(bindingHeader.getRoot());
        binding.listLastNews.setAdapter(mediumNewsAdapter);
        //disable auto scrolling
        binding.listLastNews.setHasFixedSize(true);

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

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Bundle bundle = new Bundle();
                bundle.putString(WEB_URL, url);
                Navigation.findNavController(view).navigate(R.id.action_fragmentNewsContent_to_fragmentWebView, bundle);
                return true;
            }
        });


        binding.listLastNews.addOnScrollListener(scrollListener);

        initIcons();
        presenterNewsContent.init(newsId);
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initContent(NewsContentModel model) {
//        final String mimeType = "text/html";
//        final String encoding = "UTF-8";

        bindingHeader.contentWeb.loadUrl(model.getContent());
    }

    @Override
    public void initLastNews(List<NewsSimpleModel> items, int message) {
        mediumNewsAdapter.removeHeaders();

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            mediumNewsAdapter.withHeader(bindingHeader.getRoot());

            mediumNewsAdapter.init_items(items);
        }

    }

    @Override
    public void addLastNews(List<NewsSimpleModel> items, int message) {
        mediumNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            mediumNewsAdapter.add_all(items);

        }
    }


    @Override
    public void initTags(List<String> tags) {
//        tags.add("Манчестер Юнайтед");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (String tag : tags) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_tag, null, false);
            chip.setText(tag);
//            chip.setChipBackgroundColorResource(R.color.chip_color6);


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

        binding.iconBookmark.setImageResource(
                bindingHeader.getModel().isWished() ?
                        R.drawable.bookmark_active :
                        R.drawable.bookmark_inactive);

        binding.iconBookmark.setOnClickListener(v -> {
            boolean isWished = bindingHeader.getModel().isWished();
            bindingHeader.getModel().setWished(!isWished);

            binding.iconBookmark.setImageResource(
                    bindingHeader.getModel().isWished ?
                            R.drawable.bookmark_active :
                            R.drawable.bookmark_inactive
            );
            //delete or inser news to favourites in another thread
            new Thread(() -> {
                if (isWished) {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .delete(bindingHeader.getModel().getNewsId());
                } else {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .insert(Converters
                                    .fromContentNewstoFavouritesNews(bindingHeader.getModel()));
                }
            }).start();
        });

        binding.iconShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = bindingHeader.getModel().getOriginalUrl();
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent,
                    MyUtil.getLocalizedString(getContext(), R.string.text_share)
            ));
        });
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

                mediumNewsAdapter.removeFooter();
                mediumNewsAdapter.showLoading();
                presenterNewsContent.getContinue();
            }
        }

    };

    //go navigation to BaseView first Content is called
    static class MyAsycTask extends AsyncTask<Void, Void, Void> {
        Activity activity;
        int count;
        NavController controller;

        public MyAsycTask(Activity activity, NavController controller) {
            this.activity = activity;
            this.count = count;
            this.controller = controller;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final NavController controller = Navigation.findNavController(activity, R.id.nav_host_fragment);
            for (int i = 0; i < MySettings.getInstance().getContentStack(); i++) {
                System.out.println("PopBackPressed");
                controller.popBackStack();
            }
            MySettings.getInstance().resetStack();

            return null;
        }
    }


}