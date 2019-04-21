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
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;

public class FragmentMedia extends Fragment {

    //TODO: Constants here


    //TODO: BaseClass views
    TabLayout tabLayout;
    FragmentManager fragmentManager;

    //TODO: variables here
    WindowMediaBinding binding;
    int position = -1;

    //adapters
    MediaPagerAdapter pagerAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CALLBACK_LOG, "FragmentMedia: onCreate");

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
        pagerAdapter = new MediaPagerAdapter(getContext(), getChildFragmentManager(), 3);
        binding.mediaPager.setAdapter(pagerAdapter);
//        binding.mediaPager.addOnPageChangeListener(this);
        binding.tabMedia.setupWithViewPager(binding.mediaPager);
        binding.mediaPager.setOffscreenPageLimit(1);


        if (position != -1)
            binding.mediaPager.setCurrentItem(position);
        //*****************************************************************************

        //transfarmation in viewPager
        binding.mediaPager.setPageTransformer(true, zoomOutTransformation);

    }

    //TODO: override methods

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(DELETE_LOG, "setUserVisibleHint: " + isVisibleToUser);


    }


    //#################################################################

    //TODO: all methods from IFragmentMedia interface

    //#################################################################


    //TODO: all methods from TabLayout interface


    //#################################################################

    //TODO: Additional methods

    //Inside method setPosition:
    public void setPosition(int position) {
        this.position = position;
    }

    //Inside method setPosition:

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
