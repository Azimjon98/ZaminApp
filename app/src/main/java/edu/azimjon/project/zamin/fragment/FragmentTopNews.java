package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsFeedBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;

public class FragmentTopNews extends Fragment implements IFragmentTopNews, SwipeRefreshLayout.OnRefreshListener, MediumNewsAdapter.ScrollingState {

    //TODO: Constants here
    LinearLayoutManager manager;
    boolean isConnected_to_Net = true;


    //TODO: variables here
    WindowTopNewsBinding binding;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;
    PresenterTopNews presenterTopNews;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;
    boolean isContentLoaded = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (presenterTopNews == null)
            presenterTopNews = new PresenterTopNews(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_top_news, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists
        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

            binding.listTopNews.setLayoutManager(manager);
            mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
            mediumNewsAdapter.setScrollingState(this);
            binding.listTopNews.setAdapter(mediumNewsAdapter);
            binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            binding.listTopNews.setHasFixedSize(true);

            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listTopNews, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listTopNews, false);


            binding.swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            binding.swiper.setRefreshing(true);
            presenterTopNews.init();
        }else{
            reloadContent();
        }

        //*****************************************************************************
        binding.swiper.setOnRefreshListener(this);
        binding.listTopNews.addOnScrollListener(scrollListener);
    }

    private void reloadContent(){
        if (binding == null)
            return;

        mediumNewsAdapter.notifyDataSetChanged();
    }


    //TODO: override methods


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            reloadContent();
    }

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
            presenterTopNews.init();
        else
            binding.swiper.setRefreshing(false);
    }


    //#################################################################


    //TODO: all methods from interface

    @Override
    public void initNews(List<NewsSimpleModel> items, int message) {
        binding.swiper.setRefreshing(false);

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());

            return;
        }

        if (message == MESSAGE_OK) {
            mediumNewsAdapter.removeHeaders();

            mediumNewsAdapter.init_items(items);

        }

    }


    @Override
    public void addNews(List<NewsSimpleModel> items, int message) {
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


    //#################################################################

    //TODO: Additional methods


    //#################################################################

    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(NetworkStateChangedEvent event) {
        if (event.state == NETWORK_STATE_CONNECTED && !isConnected_to_Net) {
            binding.swiper.setRefreshing(true);
            presenterTopNews.init();
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

//            if (isScrolling && (visible_items + scrollout_items == total_items) && !isLoading) {
            if (isScrolling && (visible_items + scrollout_items == total_items) && !isLoading) {
                isScrolling = false;
                isLoading = true;

                mediumNewsAdapter.removeFooter();
                mediumNewsAdapter.showLoading();
                presenterTopNews.getContinue();
            }
        }

    };


    @Override
    public void scrollingStarts() {
        Log.d(DELETE_LOG, "scrollingStarts");
//        isLoading = true;
//        mediumNewsAdapter.notify

//        mediumNewsAdapter.removeFooter();
//        mediumNewsAdapter.showLoading();
//        presenterTopNews.getContinue();
    }
}