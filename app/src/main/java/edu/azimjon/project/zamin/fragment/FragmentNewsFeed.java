package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
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
import edu.azimjon.project.zamin.adapter.ContentAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.adapter.SmallNewsAdapter;
import edu.azimjon.project.zamin.adapter.MainNewsPagerAdapter;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsFeedBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsFeedBinding;
import edu.azimjon.project.zamin.events.MyNetworkEvents;
import edu.azimjon.project.zamin.events.MyOnMoreNewsEvents;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;

import static com.arlib.floatingsearchview.util.Util.dpToPx;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;

public class FragmentNewsFeed extends Fragment implements IFragmentNewsFeed, ViewPager.OnPageChangeListener {

    public interface MyInterface {
        void scrollEnded();
    }


    //TODO: Constants here
    LinearLayoutManager manager;
    List<NewsCategoryModel> categoryModels;
    boolean isConnected_to_Net = true;


    //TODO: variables here
    WindowNewsFeedBinding binding;
    HeaderWindowNewsFeedBinding bindingHeader;
    PresenterNewsFeed presenterNewsFeed;

    //adapters
    CategoryNewsAdapter categoryNewsAdapter;

    MainNewsPagerAdapter mainNewsPagerAdapter;

    SmallNewsAdapter smallNewsAdapter;

    AudioNewsAdapter audioNewsAdapter;

    VideoNewsAdapter videoNewsAdapter;

    ContentAdapter lastContinueNewsAdapter;

    //#####################################################################

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;

    int total_items, visible_items, scrollout_items;

    @Override
    public void onAttach(Context context) {
        Log.d(CALLBACK_LOG, "FragmentNewsFeed: onAttach");

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(CALLBACK_LOG, "FragmentNewsFeed: onDetach");

        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onCreate: ");

        super.onCreate(savedInstanceState);
        presenterNewsFeed = new PresenterNewsFeed(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onCreateView: ");

        binding = DataBindingUtil.inflate(inflater, R.layout.window_news_feed, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(Constants.CALLBACK_LOG, "FragmentNewsFeed onViewCreated: ");

        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.listLastNewsContinue.setLayoutManager(manager);
        lastContinueNewsAdapter = new ContentAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listLastNewsContinue.setAdapter(lastContinueNewsAdapter);

        binding.listLastNewsContinue.addOnScrollListener(scrollListener);

        bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_feed, binding.listLastNewsContinue, false);
        lastContinueNewsAdapter.withHeader(bindingHeader.getRoot());


        //*****************************************************************************

        //TODO: Header binding initializators
        bindingHeader.listCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryNewsAdapter = new CategoryNewsAdapter(getContext(), new ArrayList<NewsCategoryModel>());
        bindingHeader.listCategory.setAdapter(categoryNewsAdapter);

        mainNewsPagerAdapter = new MainNewsPagerAdapter(getContext());
        bindingHeader.mainNewsPager.setAdapter(mainNewsPagerAdapter);
        bindingHeader.mainNewsPager.setClipToPadding(false);
        bindingHeader.mainNewsPager.setPadding(48, 0, 48, 0);
        bindingHeader.mainNewsPager.setPageMargin(36);
        bindingHeader.mainNewsPager.setCurrentItem(1);
        bindingHeader.mainNewsPager.addOnPageChangeListener(this);

        bindingHeader.listLastNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        smallNewsAdapter = new SmallNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        bindingHeader.listLastNews.setAdapter(smallNewsAdapter);

        bindingHeader.listAudioNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        bindingHeader.listAudioNews.setAdapter(audioNewsAdapter);

        bindingHeader.listVideoNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        bindingHeader.listVideoNews.setAdapter(videoNewsAdapter);

        //####################################################################################


        //TODO: categories observable here:
        CategoryNewsDatabase
                .getInstance(getContext())
                .getDao()
                .getAllEnabledCategories().observe(this, new Observer<List<NewsCategoryModel>>() {
            @Override
            public void onChanged(@Nullable List<NewsCategoryModel> categories) {
                categoryModels = categories;
                initCategories(categoryModels);
            }
        });

        //TODO: Clicker here

        bindingHeader.clickerAllAudio.setOnClickListener(v -> EventBus.getDefault().post(new MyOnMoreNewsEvents.MyOnMoreNewsEvent(0)));
        bindingHeader.clickerAllVideo.setOnClickListener(v -> EventBus.getDefault().post(new MyOnMoreNewsEvents.MyOnMoreNewsEvent(1)));

        //start process
        presenterNewsFeed.init();

    }

    //TODO: override methods

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


    //#################################################################


    //TODO: all methods from interface

    @Override
    public void initCategories(List<NewsCategoryModel> items) {
        categoryNewsAdapter.init_items(items);
    }

    @Override
    public void initMainNews(List<NewsSimpleModel> items) {
        mainNewsPagerAdapter.init_items(items, items.size());
        change_dots(items.size(), 0);
    }

    @Override
    public void initLastNews(List<NewsSimpleModel> items) {
        smallNewsAdapter.init_items(items.subList(0, 4));
        lastContinueNewsAdapter.init_items(items.subList(4, 10));
    }

    @Override
    public void initAudioNews(List<NewsSimpleModel> items) {
        audioNewsAdapter.init_items(items);
    }

    @Override
    public void initVideoNews(List<NewsSimpleModel> items) {
        videoNewsAdapter.init_items(items);
    }

    @Override
    public void addLastNewsContinue(List<NewsSimpleModel> items) {
        //give padding to bottom
        binding.listLastNewsContinue.setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());


        if (lastContinueNewsAdapter.getItemCount() != 0)
            lastContinueNewsAdapter.hideLoading();
        isLoading = false;

        lastContinueNewsAdapter.add_all(items);
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

    //TODO: variables


    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(MyNetworkEvents.NetworkStateChangedEvent event) {
        if (event.isState() == NETWORK_STATE_CONNECTED && !isConnected_to_Net)
            presenterNewsFeed.init();

        isConnected_to_Net = event.isState() == NETWORK_STATE_CONNECTED;
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


}