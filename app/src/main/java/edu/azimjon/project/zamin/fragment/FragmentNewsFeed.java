package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import androidx.navigation.Navigation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.NewsFeedAdapter;
import edu.azimjon.project.zamin.adapter.SmallNewsAdapter;
import edu.azimjon.project.zamin.adapter.MainNewsPagerAdapter;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsFeedBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsFeedBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.events.EventFavouriteChanged;
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.events.MyOnMoreNewsEvent;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static com.arlib.floatingsearchview.util.Util.dpToPx;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;
import static edu.azimjon.project.zamin.addition.Constants.EVENT_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;
import static edu.azimjon.project.zamin.addition.Constants.WEB_URL;

public class FragmentNewsFeed extends Fragment implements IFragmentNewsFeed, SwipeRefreshLayout.OnRefreshListener {

    //TODO: Constants here
    LinearLayoutManager manager;
    boolean isConnected_to_Net = true;
    String lastLocale = MySettings.getInstance().getLocale();


    //TODO: variables here
    WindowNewsFeedBinding binding;
    HeaderWindowNewsFeedBinding bindingHeader;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;
    PresenterNewsFeed presenterNewsFeed;

    //adapters
    CategoryNewsAdapter categoryNewsAdapter;

    MainNewsPagerAdapter mainNewsPagerAdapter;

    SmallNewsAdapter smallNewsAdapter;

    AudioNewsAdapter audioNewsAdapter;

    VideoNewsAdapter videoNewsAdapter;

    NewsFeedAdapter lastContinueNewsAdapter;

    //#####################################################################

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;
    boolean isContentLoaded = false;

    int total_items, visible_items, scrollout_items;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onCreate: ");

        super.onCreate(savedInstanceState);
        if (presenterNewsFeed == null)
            presenterNewsFeed = new PresenterNewsFeed(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onCreateView: ");

        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_news_feed, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onViewCreated: ");

        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists

        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.listLastNewsContinue.setLayoutManager(manager);
            binding.listLastNewsContinue.setItemAnimator(null);
            lastContinueNewsAdapter = new NewsFeedAdapter(getContext(), new ArrayList<SimpleNewsModel>());
            binding.listLastNewsContinue.setAdapter(lastContinueNewsAdapter);

            bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_feed, binding.listLastNewsContinue, false);
            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listLastNewsContinue, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listLastNewsContinue, false);

            lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());

            //give padding to bottom
            binding.listLastNewsContinue.setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            binding.listLastNewsContinue.setHasFixedSize(true);


            binding.swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            binding.swiper.setOnRefreshListener(this);


            //*****************************************************************************

            //TODO: Header binding initializators
            bindingHeader.listCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            categoryNewsAdapter = new CategoryNewsAdapter(getContext(), new ArrayList<CategoryNewsModel>());
            bindingHeader.listCategory.setAdapter(categoryNewsAdapter);

//            bindingHeader.mainNewsPager.setClipToPadding(false);
            bindingHeader.mainNewsPager.setPadding(MyUtil.dpToPx(16), 0, MyUtil.dpToPx(16), 0);
            bindingHeader.mainNewsPager.setPageMargin(MyUtil.dpToPx(16));
            bindingHeader.mainNewsPager.setCurrentItem(0);
            bindingHeader.mainNewsPager.setOffscreenPageLimit(1);

            bindingHeader.mainNewsPager.addOnPageChangeListener(pageListener);
            mainNewsPagerAdapter = new MainNewsPagerAdapter(getContext());
            bindingHeader.mainNewsPager.setAdapter(mainNewsPagerAdapter);

            bindingHeader.listLastNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            smallNewsAdapter = new SmallNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>());
            bindingHeader.listLastNews.setAdapter(smallNewsAdapter);

            bindingHeader.listAudioNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>(), null);
            bindingHeader.listAudioNews.setAdapter(audioNewsAdapter);

            bindingHeader.listVideoNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>());
            bindingHeader.listVideoNews.setAdapter(videoNewsAdapter);

            configureWebViews(bindingHeader.adWebFirst);
            configureWebViews(bindingHeader.adWebSecond);


            changeLanguage();
            binding.swiper.setRefreshing(true);
            presenterNewsFeed.init();
        } else {
            reloadContent();
            changeLanguage();
        }
        //####################################################################################

        //######################################################################################

        //TODO: Clicker here

        binding.listLastNewsContinue.addOnScrollListener(scrollListener);

        bindingHeader.clickerAllAudio.setOnClickListener(v -> EventBus.getDefault().
                post(new MyOnMoreNewsEvent(0)));

        bindingHeader.clickerAllVideo.setOnClickListener(v -> EventBus.getDefault().
                post(new MyOnMoreNewsEvent(0)));

        bindingNoConnection.btnRefresh.setOnClickListener(v -> {
            binding.swiper.setRefreshing(true);
            presenterNewsFeed.init();
        });

    }


    //TODO: Additional methods

    private void configureWebViews(WebView v) {
        File file = new File(getContext().getCacheDir(), "WebCache");
        v.getSettings().setJavaScriptEnabled(true);
        v.getSettings().setLoadWithOverviewMode(true);
        v.getSettings().setAllowFileAccess(true);
        v.getSettings().setAppCachePath(file.getAbsolutePath());
        v.getSettings().setAppCacheEnabled(true);

        v.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Bundle bundle = new Bundle();
                bundle.putString(WEB_URL, url);
                Navigation.findNavController(view).navigate(R.id.action_fragmentContent_to_fragmentWebView, bundle);

                return true;

            }

        });
    }

    private void updateMainNews(List<SimpleNewsModel> items, int position) {
        mainNewsPagerAdapter = new MainNewsPagerAdapter(getContext());
        bindingHeader.mainNewsPager.setAdapter(mainNewsPagerAdapter);

        mainNewsPagerAdapter.init_items(items, items.size());
        bindingHeader.mainNewsPager.setCurrentItem(position);

        change_dots(items.size(), position);
    }

    void change_dots(int count, int position) {
        bindingHeader.mainNewsDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(12), dpToPx(4));
            params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            View view = new View(getContext());
            view.setLayoutParams(params);
            if (i == position)
                view.setBackgroundResource(R.drawable.dots_active);
            else
                view.setBackgroundResource(R.drawable.dots_inactive);
            bindingHeader.mainNewsDots.addView(view);
        }
    }

    private void reloadContent() {
        if (binding == null || bindingHeader == null || lastContinueNewsAdapter == null)
            return;

        if (!isContentLoaded)
            return;

        if (!lastLocale.equals(MySettings.getInstance().getLocale())) {
            binding.swiper.setRefreshing(true);
            lastContinueNewsAdapter.clearItems();
            presenterNewsFeed.init();

        } else {
            initCategories(null, MESSAGE_OK);

            List<SimpleNewsModel> mainItems = mainNewsPagerAdapter.news;
            int position = bindingHeader.mainNewsPager.getCurrentItem();
            updateMainNews(mainItems, position);

            smallNewsAdapter.notifyDataSetChanged();
            videoNewsAdapter.notifyDataSetChanged();
            lastContinueNewsAdapter.notifyDataSetChanged();
        }

        lastLocale = MySettings.getInstance().getLocale();
    }

    private void changeLanguage() {
        //TODO: Change locale
        bindingHeader.textMainNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_main_news));
        bindingHeader.textLastNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_last_news));
        bindingHeader.textAudioNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_audio_news2));
        bindingHeader.textVideoNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_video_news));
        bindingHeader.clickerAllAudio.setText(MyUtil.getLocalizedString(getContext(), R.string.text_all));
        bindingHeader.clickerAllVideo.setText(MyUtil.getLocalizedString(getContext(), R.string.text_all));
        bindingNoConnection.textNoConnection.setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_connection));
        bindingNoConnection.btnRefresh.setText(MyUtil.getLocalizedString(getContext(), R.string.text_refresh));
    }
    //#################################################################

    //TODO: override methods

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRefresh() {
        if (isConnected_to_Net)
            presenterNewsFeed.init();
        else
            binding.swiper.setRefreshing(false);
    }

    //#################################################################


    //TODO: all methods from interface

//    public void initCategories(List<CategoryNewsModel> items) {
//        lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());
//        categoryNewsAdapter.initItems(items);
//    }

    @Override
    public void initCategories(List<CategoryNewsModel> items, int messege) {
        if (messege == MESSAGE_NO_CONNECTION) {
            binding.swiper.setRefreshing(false);
            lastContinueNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
        } else {
            lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());

            if (NavigationActivity.enabledCategoryModels.size() == 0)
                bindingHeader.listCategory.setVisibility(View.GONE);

            else {
                bindingHeader.listCategory.setVisibility(View.VISIBLE);
                categoryNewsAdapter.initItems(NavigationActivity.enabledCategoryModels);
            }

        }
    }

    @Override
    public void initMainNews(List<SimpleNewsModel> items, int message) {

        if (message == MESSAGE_NO_CONNECTION) {
            binding.swiper.setRefreshing(false);
            lastContinueNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());

        } else if (message == MESSAGE_OK) {
            lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());
            updateMainNews(items, 0);
        }
    }

    @Override
    public void initLastAndContinueNews(List<SimpleNewsModel> items, int message) {
        binding.swiper.setRefreshing(false);

        if (message == MESSAGE_NO_CONNECTION) {
            return;
        }

        if (message == MESSAGE_OK) {
            try {
                smallNewsAdapter.initItems(items.subList(0, 4));
                lastContinueNewsAdapter.initItems(items.subList(4, 10));
                isContentLoaded = true;
            } catch (Exception e) {
                Log.d(ERROR_LOG, "" + e.getMessage());
            }
        }
    }

    @Override
    public void initAudioNews(List<SimpleNewsModel> items, int message) {

        if (message == MESSAGE_NO_CONNECTION) {
            bindingHeader.audioLay.setVisibility(View.GONE);
            return;
        }

        if (message == MESSAGE_OK) {
            bindingHeader.audioLay.setVisibility(View.VISIBLE);
            audioNewsAdapter.initItems(items);
        }

    }

    @Override
    public void initVideoNews(List<SimpleNewsModel> items, int message) {

        if (message == MESSAGE_NO_CONNECTION) {
            bindingHeader.videoLay.setVisibility(View.GONE);
            return;
        }

        if (message == MESSAGE_OK) {
            bindingHeader.videoLay.setVisibility(View.VISIBLE);
            videoNewsAdapter.initItems(items);
            bindingHeader.adWebFirst.loadUrl("http://m.zamin.uz/api/v1/adv.php?id=23");
            bindingHeader.adWebSecond.loadUrl("http://m.zamin.uz/api/v1/adv.php?id=24");
        }


    }

    @Override
    public void addLastNewsContinue(List<SimpleNewsModel> items, int message) {
        isLoading = false;
        lastContinueNewsAdapter.hideLoading();

        if (message == MESSAGE_NO_CONNECTION) {
            lastContinueNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        } else if (message == MESSAGE_OK) {
            lastContinueNewsAdapter.addItems(items);
        }

    }

    //#################################################################


    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(NetworkStateChangedEvent event) {
//        if (event.state == NETWORK_STATE_CONNECTED) {
//            binding.swiper.setRefreshing(true);
//            presenterNewsFeed.init();
//        }
        isConnected_to_Net = (event.state == NETWORK_STATE_CONNECTED);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(EventFavouriteChanged event) {
        Log.d(EVENT_LOG, "Favourite Changed (NewsFeed): ");
        reloadContent();
    }

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


                lastContinueNewsAdapter.showLoading();
                presenterNewsFeed.getLastNewsContinue();
            }
        }

    };

    ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            change_dots(mainNewsPagerAdapter.getCount(), i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };


}