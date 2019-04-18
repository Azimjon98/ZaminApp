package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import android.widget.AbsListView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
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
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.events.MyOnMoreNewsEvent;
import edu.azimjon.project.zamin.events.PlayerStateEvent;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static com.arlib.floatingsearchview.util.Util.dpToPx;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;
import static edu.azimjon.project.zamin.addition.Constants.EVENT_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.*;

public class FragmentNewsFeed extends Fragment implements IFragmentNewsFeed, SwipeRefreshLayout.OnRefreshListener {

    //TODO: Constants here
    LinearLayoutManager manager;
    List<NewsCategoryModel> categoryModels;
    boolean isConnected_to_Net = true;


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
    public void onAttach(Context context) {
        Log.d(CALLBACK_LOG, "FragmentNewsFeed: onAttach");

        super.onAttach(context);
    }

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
            lastContinueNewsAdapter = new NewsFeedAdapter(getContext(), new ArrayList<NewsSimpleModel>());
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
            categoryNewsAdapter = new CategoryNewsAdapter(getContext(), new ArrayList<NewsCategoryModel>());
            bindingHeader.listCategory.setAdapter(categoryNewsAdapter);

            bindingHeader.mainNewsPager.setClipToPadding(false);
            bindingHeader.mainNewsPager.setPadding(48, 0, 48, 0);
            bindingHeader.mainNewsPager.setPageMargin(36);
            bindingHeader.mainNewsPager.setCurrentItem(1);
            bindingHeader.mainNewsPager.addOnPageChangeListener(pageListener);
            mainNewsPagerAdapter = new MainNewsPagerAdapter(getContext());
            bindingHeader.mainNewsPager.setAdapter(mainNewsPagerAdapter);

            bindingHeader.listLastNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            smallNewsAdapter = new SmallNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
            bindingHeader.listLastNews.setAdapter(smallNewsAdapter);

            bindingHeader.listAudioNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>(), null);
            bindingHeader.listAudioNews.setAdapter(audioNewsAdapter);

            bindingHeader.listVideoNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
            bindingHeader.listVideoNews.setAdapter(videoNewsAdapter);

            //TODO: Change locale
            bindingHeader.textMainNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_main_news));
            bindingHeader.textLastNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_last_news));
            bindingHeader.textAudioNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_audio_news2));
            bindingHeader.textVideoNews.setText(MyUtil.getLocalizedString(getContext(), R.string.text_video_news));
            bindingHeader.clickerAllAudio.setText(MyUtil.getLocalizedString(getContext(), R.string.text_all));
            bindingHeader.clickerAllVideo.setText(MyUtil.getLocalizedString(getContext(), R.string.text_all));


            binding.swiper.setRefreshing(true);
            presenterNewsFeed.init();
        }
        //####################################################################################


        //TODO: categories observable here:
        CategoryNewsDatabase
                .getInstance(getContext())
                .getDao()
                .getAllEnabledCategoriesLive().
                observe(this, new Observer<List<NewsCategoryModel>>() {
                    @Override
                    public void onChanged(@Nullable List<NewsCategoryModel> categories) {
                        categoryModels = categories;
                        initCategories(categoryModels);

                        if (categories.size() == 0)
                            bindingHeader.listCategory.setVisibility(View.GONE);
                        else
                            bindingHeader.listCategory.setVisibility(View.VISIBLE);
                    }
                });

        //######################################################################################

        //TODO: Clicker here

        binding.listLastNewsContinue.addOnScrollListener(scrollListener);

        bindingHeader.clickerAllAudio.setOnClickListener(v -> EventBus.getDefault().
                post(new MyOnMoreNewsEvent(0)));

        bindingHeader.clickerAllVideo.setOnClickListener(v -> EventBus.getDefault().
                post(new MyOnMoreNewsEvent(1)));

    }


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

    public void initCategories(List<NewsCategoryModel> items) {
        categoryNewsAdapter.init_items(items);
    }

    int index = 1;

    @Override
    public void initMainNews(List<NewsSimpleModel> items, int message) {
        binding.swiper.setRefreshing(false);

        if (message == MESSAGE_NO_CONNECTION) {
            lastContinueNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            mainNewsPagerAdapter = new MainNewsPagerAdapter(getContext());
            bindingHeader.mainNewsPager.setAdapter(mainNewsPagerAdapter);

            lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());
            mainNewsPagerAdapter.init_items(items, items.size());

            change_dots(items.size(), 0);
        }
        index++;
    }

    @Override
    public void initLastAndContinueNews(List<NewsSimpleModel> items, int message) {
        binding.swiper.setRefreshing(false);

        if (message == MESSAGE_NO_CONNECTION) {
            return;
        }

        if (message == MESSAGE_OK) {
            try {
                smallNewsAdapter.init_items(items.subList(0, 4));
                lastContinueNewsAdapter.init_items(items.subList(4, 10));
            } catch (Exception e) {
                Log.d(ERROR_LOG, "" + e.getMessage());
            }
        }
    }

    @Override
    public void initAudioNews(List<NewsSimpleModel> items, int message) {

        if (message == MESSAGE_NO_CONNECTION) {
            bindingHeader.audioLay.setVisibility(View.GONE);
            return;
        }

        if (message == MESSAGE_OK) {
            bindingHeader.audioLay.setVisibility(View.VISIBLE);
            audioNewsAdapter.init_items(items);
        }

    }

    @Override
    public void initVideoNews(List<NewsSimpleModel> items, int message) {

        if (message == MESSAGE_NO_CONNECTION) {
            bindingHeader.videoLay.setVisibility(View.GONE);
            return;
        }

        if (message == MESSAGE_OK) {
            bindingHeader.videoLay.setVisibility(View.VISIBLE);
            videoNewsAdapter.init_items(items);
        }


    }

    @Override
    public void addLastNewsContinue(List<NewsSimpleModel> items, int message) {
        lastContinueNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            Log.d(DELETE_LOG, "addLastNewsContinue, MESSAGE_NO_CONNECTION");
            lastContinueNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            lastContinueNewsAdapter.add_all(items);
        }

    }

    //#################################################################

    //TODO: Additional methods

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

    //#################################################################


    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(NetworkStateChangedEvent event) {
        Log.d(EVENT_LOG, "subscribe: on_network_changed: " + isContentLoaded + " state: " + (event.state == NETWORK_STATE_CONNECTED));
        if (event.state == NETWORK_STATE_CONNECTED) {
            binding.swiper.setRefreshing(true);
            presenterNewsFeed.init();

        }

        isConnected_to_Net = (event.state == NETWORK_STATE_CONNECTED);
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

                lastContinueNewsAdapter.removeFooter();
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