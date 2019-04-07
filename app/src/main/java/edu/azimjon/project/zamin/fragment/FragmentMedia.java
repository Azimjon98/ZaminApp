package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediaPagerAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.databinding.WindowMediaBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterMedia;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentMedia;
import timber.log.Timber;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class FragmentMedia extends Fragment implements IFragmentMedia, ViewPager.OnPageChangeListener {

    //TODO: Constants here


    //TODO: BaseClass views
    TabLayout tabLayout;
    FragmentManager fragmentManager;

    //TODO: variables here
    WindowMediaBinding binding;
    PresenterMedia presenterMedia;
    int position = 0;

    //adapters
    MediaPagerAdapter pagerAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CALLBACK_LOG, "FragmentMedia: onCreate");

        presenterMedia = new PresenterMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "FragmentMedia: onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.window_media, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(CALLBACK_LOG, "FragmentMedia: onViewCreated");


        //initialize adapters and append to lists or pagers

        pagerAdapter = new MediaPagerAdapter(getContext(), getChildFragmentManager(), 3);
        binding.mediaPager.setAdapter(pagerAdapter);
        binding.mediaPager.addOnPageChangeListener(this);
        binding.tabMedia.setupWithViewPager(binding.mediaPager);
        binding.mediaPager.setCurrentItem(position);


        //*****************************************************************************

        presenterMedia.init();
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from IFragmentMedia interface

    @Override
    public void initAudio(List<NewsSimpleModel> items) {

    }

    @Override
    public void initVideo(List<NewsSimpleModel> items) {

    }

    @Override
    public void initGallery(List<NewsSimpleModel> items) {

    }

    //#################################################################


    //TODO: all methods from TabLayout interface

    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.d(CALLBACK_LOG, "FragmentMedia: onPageScrolled");
    }

    @Override
    public void onPageSelected(int i) {
        Log.d(CALLBACK_LOG, "FragmentMedia: onPageSelected");


    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d(CALLBACK_LOG, "FragmentMedia: onPageScrollStateChanged");


    }


    //#################################################################

    //TODO: Additional methods

    //Inside method setPosition:
    public void setPosition(int position) {
        this.position = position;
    }


    //#################################################################


}
