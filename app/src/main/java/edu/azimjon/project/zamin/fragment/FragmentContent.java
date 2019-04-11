package edu.azimjon.project.zamin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.ContentPagerAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.events.MyOnMoreNewsEvents;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class FragmentContent extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    //TODO: Variables
    ContentPagerAdapter contentPagerAdapter;


    //TODO: Views here
    BottomNavigationView navigationView;
    TextView toolbarTitle;
    ViewPager contentPager;
    AppBarLayout appBarLayout;
    ImageView searchIcon;
    ImageView profileIcon;

    View baseView;


    //TODO: initial fragments

    private int currenrItem = R.id.menu_news_feed;
    private int savedId = -1;

    @Override
    public void onAttach(Context context) {
        Log.d(CALLBACK_LOG, "FragmentContent: onAttach");

        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d(CALLBACK_LOG, "FragmentContent: onDetach");

        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(CALLBACK_LOG, "FragmentContent: onPause");

        super.onPause();
    }

    @Override
    public void onStart() {
        Log.d(CALLBACK_LOG, "FragmentContent: onStart");

        super.onStart();
    }

    @Override
    public void onDestroy() {
        Log.d(CALLBACK_LOG, "FragmentContent: onDestroy");

        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "FragmentContent: onCreate");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        Log.d(CALLBACK_LOG, "FragmentContent onDestroyView");

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        Log.d(CALLBACK_LOG, "FragmentContent onResume");

        super.onResume();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "FragmentContent: onCreateView");

        if (baseView == null) {
            baseView = inflater.inflate(R.layout.window_content, container, false);
        }

        return baseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG, "FragmentContent: onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        navigationView = view.findViewById(R.id.bottom_navigation);
        appBarLayout = view.findViewById(R.id.app_bar);
        searchIcon = view.findViewById(R.id.toolbar_search);
        profileIcon = view.findViewById(R.id.toolbar_ic_more);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        if (contentPagerAdapter == null)
            contentPagerAdapter = new ContentPagerAdapter(getContext(), getChildFragmentManager(), 4);
        contentPager = view.findViewById(R.id.content_pager);
        contentPager.setAdapter(contentPagerAdapter);
        contentPagerAdapter.setPlayer(view.findViewById(R.id.player_lay));
        contentPager.setOffscreenPageLimit(1);

        //when this window comes back continue with old navigation index
        if (savedId == -1) {
            navigationView.setSelectedItemId(savedId);
        }

        //getting height of navigation view
        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                MySettings.getInstance().setNavigationHeight(navigationView.getHeight());
            }
        });


        //declare toolbar icon touch listeners
        searchIcon.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentContent_to_fragmentSearchNews));

        profileIcon.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentContent_to_fragmentProfile));

        navigationView.setOnNavigationItemSelectedListener(this);
    }


    //TODO: override methods

    //#################################################################


    //TODO: all methods from interface

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        savedId = navigationView.getSelectedItemId();
        toolbarTitle.setText(item.getTitle());
        switch (item.getItemId()) {
            case R.id.menu_news_feed:
                contentPager.setCurrentItem(0);
                appBarLayout.setElevation(12f);
                return true;
            case R.id.menu_topnews:
                contentPager.setCurrentItem(1);
                appBarLayout.setElevation(12f);
                return true;
            case R.id.menu_favourites:
                contentPager.setCurrentItem(2);
                appBarLayout.setElevation(12f);
                return true;
            case R.id.menu_media:
                contentPager.setCurrentItem(3);
                appBarLayout.setElevation(0f);
                appBarLayout.setTranslationZ(0f);
                return true;
        }
        return false;
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################

    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_more_news(MyOnMoreNewsEvents.MyOnMoreNewsEvent event) {
        navigationView.setSelectedItemId(R.id.menu_media);
        contentPagerAdapter.setPositionOfMediaNews(event.getPosition());

    }

}
