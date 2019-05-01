package edu.azimjon.project.zamin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyChromeClient;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;
import static edu.azimjon.project.zamin.addition.Constants.KEY_OPEN_GALLERY_ITEM;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_TAG;
import static edu.azimjon.project.zamin.addition.Constants.WEB_URL;

public class FragmentNewsContent extends Fragment implements IFragmentNewsContent {
    //TODO: Constants here
    String newsId;
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
    boolean mShouldPause = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsId = getArguments().getString(KEY_NEWS_ID);
        model = getArguments().getParcelable(KEY_NEWS_MODEL);

        if (presenterNewsContent == null)
            presenterNewsContent = new PresenterNewsContent(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_news_content, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.iconBack.setOnClickListener(v -> {
            new MyAsycTask(Navigation.findNavController(v)).execute();
        });

        //initialize adapters and append to lists

        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.listLastNews.setLayoutManager(manager);
            mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());

            bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_content, binding.listLastNews, false);
            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listLastNews, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listLastNews, false);

            bindingHeader.title.setText(model.getTitle());

            mediumNewsAdapter.withHeader(bindingHeader.getRoot());
            binding.listLastNews.setAdapter(mediumNewsAdapter);
            //disable auto scrolling
            binding.listLastNews.setHasFixedSize(true);

            //*****************************************************************************

            bindingHeader.contentWeb.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    bindingHeader.progress.setVisibility(View.VISIBLE);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    bindingHeader.progress.setVisibility(View.GONE);
                }


                @Override
                public void onLoadResource(WebView view, String url) {
                    super.onLoadResource(view, url);
                    if (url.contains("youtube.com")) mShouldPause = true;

                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Bundle bundle = new Bundle();
                    bundle.putString(WEB_URL, url);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentNewsContent_to_fragmentWebView, bundle);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);

                    Log.d(DELETE_LOG, "onReceivedError");
                }

                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                    super.onReceivedHttpError(view, request, errorResponse);
                    Log.d(DELETE_LOG, "onReceivedHttpError: " + request.getUrl() + " \n" + errorResponse.getMimeType() + " " + errorResponse.getStatusCode());
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    Log.d(DELETE_LOG, "onReceivedSslError");
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);

                    Log.d(DELETE_LOG, "onReceivedError");

                }
            });

            //TODO: Header binding initializators
            // Enable Javascript
            File file = new File(getContext().getCacheDir(), "WebCache");
            bindingHeader.contentWeb.getSettings().setLoadWithOverviewMode(true);
            bindingHeader.contentWeb.getSettings().setAllowFileAccess(true);
            bindingHeader.contentWeb.getSettings().setAppCachePath(file.getAbsolutePath());
            bindingHeader.contentWeb.getSettings().setAppCacheEnabled(true);
            bindingHeader.contentWeb.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            bindingHeader.contentWeb.getSettings().setSupportZoom(true);
            bindingHeader.contentWeb.getSettings().setBuiltInZoomControls(true);
            bindingHeader.contentWeb.getSettings().setSupportMultipleWindows(true);
            bindingHeader.contentWeb.getSettings().setDomStorageEnabled(true);
            bindingHeader.contentWeb.clearCache(true);
            bindingHeader.contentWeb.clearHistory();
            bindingHeader.contentWeb.getSettings().setJavaScriptEnabled(true);
            bindingHeader.contentWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            bindingHeader.contentWeb.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36");

//            bindingHeader.contentWeb.getSettings().setAllowFileAccessFromFileURLs(true);
//            bindingHeader.contentWeb.getSettings().setAllowUniversalAccessFromFileURLs(true);
//            bindingHeader.contentWeb.clearCache(true);
//            bindingHeader.contentWeb.clearHistory();
//            bindingHeader.contentWeb.getSettings().setAllowContentAccess(true);
//            bindingHeader.contentWeb.getSettings().setDomStorageEnabled(true);
//            bindingHeader.contentWeb.getSettings().setJavaScriptEnabled(true); // enable javascript
//            bindingHeader.contentWeb.getSettings().setBuiltInZoomControls(true);
//            bindingHeader.contentWeb.getSettings().setSupportZoom(true);
//            bindingHeader.contentWeb.getSettings().setLoadWithOverviewMode(true);
//            bindingHeader.contentWeb.getSettings().setUseWideViewPort(true);
//
//            bindingHeader.contentWeb.getSettings().setBuiltInZoomControls(true);
//            bindingHeader.contentWeb.getSettings().setDisplayZoomControls(false);
//
//            bindingHeader.contentWeb.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//            bindingHeader.contentWeb.setScrollbarFadingEnabled(false);
//            bindingHeader.contentWeb.getSettings().setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36");



//            bindingHeader.contentWeb.setWebChromeClient(new MyChromeClient(getActivity()));


            presenterNewsContent.init(newsId);

        } else {
            if (mediumNewsAdapter != null)
                mediumNewsAdapter.notifyDataSetChanged();
        }

        binding.listLastNews.addOnScrollListener(scrollListener);

        initIcons();
    }

    //TODO: override methods

    @Override
    public void onPause() {
        super.onPause();

        if (mShouldPause) {
            bindingHeader.contentWeb.onPause();
        }
        mShouldPause = false;
    }


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initContent(NewsContentModel model) {
        if (getContext() == null)
            return;

        bindingHeader.setModel(model);

        bindingHeader.contentWeb.loadUrl(model.getContent());
    }

    @Override
    public void initLastNews(List<NewsSimpleModel> items, int message) {
        if (getContext() == null)
            return;
        mediumNewsAdapter.removeHeaders();

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            //check if news contains news with this id
            List<NewsSimpleModel> news = new ArrayList<>(items);
            for (NewsSimpleModel i : news) {
                if (i.getNewsId().equals(newsId)) {
                    news.remove(i);
                    break;
                }
            }

            mediumNewsAdapter.withHeader(bindingHeader.getRoot());
            mediumNewsAdapter.init_items(news);
        }

    }

    @Override
    public void addLastNews(List<NewsSimpleModel> items, int message) {
        if (getContext() == null)
            return;
        mediumNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            //check if news contains news with this id
            List<NewsSimpleModel> news = new ArrayList<>(items);
            for (NewsSimpleModel i : news) {
                if (i.getNewsId().equals(newsId)) {
                    news.remove(i);
                    break;
                }
            }


            mediumNewsAdapter.add_all(news);

        }
    }


    @Override
    public void initTags(List<String> tags) {
        if (getContext() == null)
            return;
        if (tags.size() != 0) {
            bindingHeader.tagsGroup.setVisibility(View.VISIBLE);
        } else {
            bindingHeader.tagsGroup.setVisibility(View.GONE);
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (String tag : tags) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_tag, bindingHeader.tagsGroup, false);
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

        List<String> allFavouriteIds;
        allFavouriteIds = NavigationActivity.getFavouritesIds();
        if (allFavouriteIds.contains(model.getNewsId())) {
            model.setWished(true);
        }else{
            model.setWished(false);
        }

        binding.iconBookmark.setImageResource(
                model.isWished() ?
                        R.drawable.bookmark_active :
                        R.drawable.bookmark_inactive);

        binding.iconBookmark.setOnClickListener(v -> {
            boolean isWished = model.isWished();
            model.setWished(!isWished);

            binding.iconBookmark.setImageResource(
                    model.isWished ?
                            R.drawable.bookmark_active :
                            R.drawable.bookmark_inactive
            );
            //delete or inser news to favourites in another thread
            new Thread(() -> {
                if (isWished) {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .delete(model.getNewsId());
                } else {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .insert(Converters
                                    .fromContentNewstoFavouritesNews(model));
                }
            }).start();
        });

        binding.iconShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = bindingHeader.getModel().getOriginalUrl();
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent,
                    MyUtil.getLocalizedString(v.getContext(), R.string.text_share)
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
        NavController controller;

        public MyAsycTask(NavController controller) {
            this.controller = controller;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            controller.popBackStack(MySettings.getInstance().getWhichIdCallsContent(), false);

            return null;
        }
    }

}