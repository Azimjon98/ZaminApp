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
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowVideoInsideMediaBinding;
import edu.azimjon.project.zamin.events.EventFavouriteChanged;
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterVideoInMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentVideoInMedia;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.EVENT_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;

public class FragmentVideoInMedia extends Fragment implements IFragmentVideoInMedia, SwipeRefreshLayout.OnRefreshListener {

    //TODO: Constants here
    LinearLayoutManager manager;
    boolean isConnected_to_Net = true;
    String lastLocale = MySettings.getInstance().getLocale();

    //TODO: variables here
    WindowVideoInsideMediaBinding binding;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;
    View viewHeader;

    //adapters
    VideoNewsAdapter videoNewsAdapter;
    PresenterVideoInMedia presenterVideoInMedia;

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;
    boolean isContentLoaded = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterVideoInMedia = new PresenterVideoInMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_video_inside_media, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //initialize adapters and append to lists

        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.listVideo.setLayoutManager(manager);
            videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>());
            viewHeader = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.header_window_video_inside_media,
                            binding.listVideo,
                            false);

            videoNewsAdapter.withHeader(viewHeader);
            binding.listVideo.setAdapter(videoNewsAdapter);
            binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            binding.listVideo.setHasFixedSize(true);


            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listVideo, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listVideo, false);

            binding.swiper.setRefreshing(true);
            presenterVideoInMedia.init();
        }else{
            reloadContent();
        }

        binding.swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        binding.swiper.setOnRefreshListener(this);


        //*****************************************************************************

        binding.swiper.setOnRefreshListener(this);
        binding.listVideo.addOnScrollListener(scrollListener);

        bindingNoConnection.btnRefresh.setOnClickListener(v -> {
            binding.swiper.setRefreshing(true);
            presenterVideoInMedia.init();
        });



        //*****************************************************************************

        //TODO: Change locale
        ((TextView) viewHeader.findViewById(R.id.text_title)).setText(MyUtil.getLocalizedString(getContext(), R.string.title_video_news));
        ((TextView) viewHeader.findViewById(R.id.text_1)).setText(MyUtil.getLocalizedString(getContext(), R.string.message_video));
        bindingNoConnection.textNoConnection.setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_connection));
        bindingNoConnection.btnRefresh.setText(MyUtil.getLocalizedString(getContext(), R.string.text_refresh));
        //############################################################
    }

    private void reloadContent(){
        System.out.println("reloadContent");
        if (binding == null || videoNewsAdapter == null || !isContentLoaded)
            return;

        if (!lastLocale.equals(MySettings.getInstance().getLocale())){
            binding.swiper.setRefreshing(true);
            videoNewsAdapter.clearItems();
            presenterVideoInMedia.init();
        }else
            videoNewsAdapter.notifyDataSetChanged();

        lastLocale = MySettings.getInstance().getLocale();
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
            presenterVideoInMedia.init();
        else
            binding.swiper.setRefreshing(false);
    }

    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initVideo(List<SimpleNewsModel> items, int message) {
        binding.swiper.setRefreshing(false);

        if (message == MESSAGE_NO_CONNECTION) {
            videoNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
            return;
        }

        else if (message == MESSAGE_OK) {
            videoNewsAdapter.withHeader(viewHeader);
            videoNewsAdapter.initItems(items);
            isContentLoaded = true;
        }

    }

    @Override
    public void addVideo(List<SimpleNewsModel> items, int message) {
        videoNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            videoNewsAdapter.withFooter(bindingFooter.getRoot());
        }

        else if (message == MESSAGE_OK) {
            videoNewsAdapter.addItems(items);

        }
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################

    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(NetworkStateChangedEvent event) {
//        if (event.state == NETWORK_STATE_CONNECTED && !isConnected_to_Net) {
//            binding.swiper.setRefreshing(false);
//            presenterVideoInMedia.init();
//        }

        isConnected_to_Net = (event.state == NETWORK_STATE_CONNECTED);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(EventFavouriteChanged event) {
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

                videoNewsAdapter.showLoading();
                presenterVideoInMedia.getContinue();
            }
        }

    };


}
