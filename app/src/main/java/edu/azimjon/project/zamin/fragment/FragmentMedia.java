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

public class FragmentMedia extends Fragment implements IFragmentMedia {

    //TODO: Constants here


    //TODO: BaseClass views
    TabLayout tabLayout;
    FragmentManager fragmentManager;

    //TODO: variables here
    WindowMediaBinding binding;
    ViewGroup player;
    PresenterMedia presenterMedia;
    int position = 0;

    //adapters
    MediaPagerAdapter pagerAdapter;

    boolean isContentLoaded;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CALLBACK_LOG, "FragmentMedia: onCreate");

        if (presenterMedia == null)
            presenterMedia = new PresenterMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "FragmentMedia: onCreateView");
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_media, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(CALLBACK_LOG, "FragmentMedia: onViewCreated");


        //initialize adapters and append to lists or pagers
        if (!isContentLoaded) {
            pagerAdapter = new MediaPagerAdapter(getContext(), getChildFragmentManager(), 3);
            binding.mediaPager.setAdapter(pagerAdapter);
//        binding.mediaPager.addOnPageChangeListener(this);
            binding.tabMedia.setupWithViewPager(binding.mediaPager);
            binding.mediaPager.setCurrentItem(position);
            pagerAdapter.setPlayer(player);
            binding.mediaPager.setOffscreenPageLimit(1);
        }
        //*****************************************************************************

        //transfarmation in viewPager
        binding.mediaPager.setPageTransformer(true, zoomOutTransformation);

    }

    //TODO: override methods

    @Override
    public void onResume() {
        super.onResume();

        if (!isContentLoaded) {
            presenterMedia.init();
            isContentLoaded = true;
        }
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.d(CALLBACK_LOG, "Media: getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(CALLBACK_LOG, "Media: setUserVisibleHint: " + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

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


    //#################################################################

    //TODO: Additional methods

    //Inside method setPosition:
    public void setPosition(int position) {
        this.position = position;
    }

    //Inside method setPosition:
    public void setPlayer(ViewGroup player) {
        this.player = player;
    }


    //#################################################################

    //TODO: argument Variables

    public ViewPager.PageTransformer zoomOutTransformation = new ViewPager.PageTransformer() {


        final float MIN_SCALE = 0.8f;
        final float MIN_ALPHA = 0.8f;

        @Override
        public void transformPage(View page, float position) {

            if (position < -1) {  // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            } else if (position <= 1) { // [-1,1]

                page.setScaleX(Math.max(MIN_SCALE, 1 - Math.abs(position)));
                page.setScaleY(Math.max(MIN_SCALE, 1 - Math.abs(position)));
                page.setAlpha(Math.max(MIN_ALPHA, 1 - Math.abs(position)));

            } else {  // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);
            }
        }


    };


}
