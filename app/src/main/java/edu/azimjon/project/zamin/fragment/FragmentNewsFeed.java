package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.SmallNewsAdapter;
import edu.azimjon.project.zamin.adapter.TopNewsPagerAdapter;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.databinding.WindowNewsFeedBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;

import static com.arlib.floatingsearchview.util.Util.dpToPx;

public class FragmentNewsFeed extends Fragment implements IFragmentNewsFeed, ViewPager.OnPageChangeListener {

    //TODO: Constants here


    //TODO: variables here
    WindowNewsFeedBinding binding;
    PresenterNewsFeed presenterNewsFeed;

    //adapters
    CategoryNewsAdapter categoryNewsAdapter;

    TopNewsPagerAdapter topNewsPagerAdapter;

    SmallNewsAdapter smallNewsAdapter;

    AudioNewsAdapter audioNewsAdapter;

    VideoNewsAdapter videoNewsAdapter;

    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterNewsFeed = new PresenterNewsFeed(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_news_feed, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        //*****************************************************************************

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
        smallNewsAdapter.init_items(items);
    }

    @Override
    public void initAudioNews(List<NewsSimpleModel> items) {
        audioNewsAdapter.init_items(items);
    }

    @Override
    public void initVideoNews(List<NewsSimpleModel> items) {
        videoNewsAdapter.init_items(items);
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


}
