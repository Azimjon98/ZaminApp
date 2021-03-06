package edu.azimjon.project.zamin.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediaPagerAdapter;
import edu.azimjon.project.zamin.mvp.presenter.PresenterMedia;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class TestActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        viewPager = findViewById(R.id.media_pager);

        pagerAdapter = new MediaPagerAdapter(this,  getSupportFragmentManager(),3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        // binding.mediaPager.setCurrentItem(1);

//        if (tabLayout != null){
//            tabLayout.setupWithViewPager(binding.mediaPager, true);
//
//            //select Video Item when opening
////            tabLayout.getTabAt(1).select();
//        }

//        binding..setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>());
//        binding.listTopNews.setAdapter(mediumNewsAdapter);
    }


    //TODO: Constants here


    //TODO: BaseClass views
    TabLayout tabLayout;

    //TODO: variables here
    PresenterMedia presenterMedia;

    //adapters
    MediaPagerAdapter pagerAdapter;


    //#####################################################################




    @Override
    public void onPageScrolled(int i, float v, int i1) {
        Log.d(CALLBACK_LOG,"FragmentMedia: onPageScrolled");
    }

    @Override
    public void onPageSelected(int i) {
        Log.d(CALLBACK_LOG,"FragmentMedia: onPageSelected");


    }

    @Override
    public void onPageScrollStateChanged(int i) {
        Log.d(CALLBACK_LOG,"FragmentMedia: onPageScrollStateChanged");


    }


    //#################################################################

    //TODO: Additional methods

    public void setTabLayout(TabLayout view){
        tabLayout = view;

    }


    //#################################################################



}
