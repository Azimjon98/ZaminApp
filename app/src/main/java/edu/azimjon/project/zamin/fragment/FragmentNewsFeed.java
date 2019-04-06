package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.adapter.SmallNewsAdapter;
import edu.azimjon.project.zamin.adapter.TopNewsPagerAdapter;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowNewsFeedBinding;
import edu.azimjon.project.zamin.events.MyNetworkEvents;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyOnScrollListener;

import static com.arlib.floatingsearchview.util.Util.dpToPx;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;

public class FragmentNewsFeed extends Fragment implements IFragmentNewsFeed, ViewPager.OnPageChangeListener {

    public interface MyInterface {
        void scrollEnded();
    }


    //TODO: Constants here
    List<NewsCategoryModel> categoryModels;
    boolean isConnected_to_Net = true;


    //TODO: variables here
    WindowNewsFeedBinding binding;
    PresenterNewsFeed presenterNewsFeed;

    //adapters
    CategoryNewsAdapter categoryNewsAdapter;

    TopNewsPagerAdapter topNewsPagerAdapter;

    SmallNewsAdapter smallNewsAdapter;

    AudioNewsAdapter audioNewsAdapter;

    VideoNewsAdapter videoNewsAdapter;

    MediumNewsAdapter lastContinueNewsAdapter;

    //#####################################################################

    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;

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

        binding.listCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryNewsAdapter = new CategoryNewsAdapter(getContext(), new ArrayList<NewsCategoryModel>());
        binding.listCategory.setAdapter(categoryNewsAdapter);

        topNewsPagerAdapter = new TopNewsPagerAdapter(getContext());
        binding.mainNewsPager.setAdapter(topNewsPagerAdapter);
        binding.mainNewsPager.setClipToPadding(false);
        binding.mainNewsPager.setPadding(48, 0, 48, 0);
        binding.mainNewsPager.setPageMargin(36);
        binding.mainNewsPager.setCurrentItem(1);
        binding.mainNewsPager.addOnPageChangeListener(this);

        binding.listLastNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        smallNewsAdapter = new SmallNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listLastNews.setAdapter(smallNewsAdapter);

        binding.listAudioNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listAudioNews.setAdapter(audioNewsAdapter);

        binding.listVideoNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listVideoNews.setAdapter(videoNewsAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.listLastNewsContinue.setLayoutManager(mLayoutManager);
        lastContinueNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listLastNewsContinue.setAdapter(lastContinueNewsAdapter);


        binding.nestedView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight()) ||
                        -scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {

                    lastContinueNewsAdapter.showLoading();
                    presenterNewsFeed.getLastNewsContinue();
                }
            }


        });

        //*****************************************************************************


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

        presenterNewsFeed.init();

    }

    //TODO: override methods

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        change_dots(topNewsPagerAdapter.getCount(), i);
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
        topNewsPagerAdapter.init_items(items, items.size());
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

        lastContinueNewsAdapter.add_items(items);
    }

    //#################################################################

    //TODO: Additional methods

    void change_dots(int count, int position) {
        binding.mainNewsDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(12), dpToPx(4));
            params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            View view = new View(getContext());
            view.setLayoutParams(params);
            if (i == position)
                view.setBackgroundResource(R.drawable.dots_active);
            else
                view.setBackgroundResource(R.drawable.dots_inactive);
            binding.mainNewsDots.addView(view);
        }
    }

    //#################################################################


    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(MyNetworkEvents.NetworkStateChangedEvent event) {
        if (event.isState() == NETWORK_STATE_CONNECTED && !isConnected_to_Net)
            presenterNewsFeed.init();

        isConnected_to_Net = event.isState() == NETWORK_STATE_CONNECTED;
    }


}