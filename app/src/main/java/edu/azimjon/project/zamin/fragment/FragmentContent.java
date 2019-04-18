package edu.azimjon.project.zamin.fragment;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.ContentPagerAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.events.MyOnMoreNewsEvent;
import edu.azimjon.project.zamin.events.PlayerStateEvent;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.*;

public class FragmentContent extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    //TODO: Variables
    ContentPagerAdapter contentPagerAdapter;


    //TODO: Views here
    BottomNavigationView navigationView;
    ViewPager contentPager;
    AppBarLayout appBarLayout;
    ImageView searchIcon;
    ImageView profileIcon;

    RelativeLayout playerView;
    TextView playerTitle, playerStartTime, playerEndTime;
    ImageView prevIcon, playIcon, nextIcon;
    SeekBar playerProgress;

    View baseView;


    //TODO: initial fragments

    private int currenrItem = R.id.menu_news_feed;
    private int savedId = -1;


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

        playerView = view.findViewById(R.id.player_lay);
        playerTitle = view.findViewById(R.id.player_title);
        prevIcon = view.findViewById(R.id.player_play);
        playerStartTime = view.findViewById(R.id.player_time_start);
        playerEndTime = view.findViewById(R.id.player_time_end);
        playIcon = view.findViewById(R.id.player_play);
        nextIcon = view.findViewById(R.id.player_next);
        playerProgress = view.findViewById(R.id.player_seek_bar);
        initPlayerIcons();

        if (contentPagerAdapter == null)
            contentPagerAdapter = new ContentPagerAdapter(getContext(), getChildFragmentManager(), 4);
        contentPager = view.findViewById(R.id.content_pager);
        contentPager.setAdapter(contentPagerAdapter);
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

        //Changing locale of menu
        navigationView.getMenu().getItem(0).setTitle(MyUtil.getLocalizedString(getContext(), R.string.menu_news_feed));
        navigationView.getMenu().getItem(1).setTitle(MyUtil.getLocalizedString(getContext(), R.string.menu_top));
        navigationView.getMenu().getItem(2).setTitle(MyUtil.getLocalizedString(getContext(), R.string.menu_favourites));
        navigationView.getMenu().getItem(3).setTitle(MyUtil.getLocalizedString(getContext(), R.string.menu_media));
    }

    //TODO: Player initialization
    private void initPlayerIcons() {
        playIcon.setOnClickListener(v -> {
            EventBus.getDefault().post(new PlayerStateEvent(PLAYER_PLAY, ""));
        });

        prevIcon.setOnClickListener(v -> {
            EventBus.getDefault().post(new PlayerStateEvent(PLAYER_PREV, ""));
        });

        nextIcon.setOnClickListener(v -> {
            EventBus.getDefault().post(new PlayerStateEvent(PLAYER_NEXT, ""));
        });
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

    //#################################################################


    //TODO: all methods from interface

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        savedId = navigationView.getSelectedItemId();
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
                EventBus.getDefault().post(new PlayerStateEvent(PLAYER_STOP, ""));
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
    public void on_more_news(MyOnMoreNewsEvent event) {
        navigationView.setSelectedItemId(R.id.menu_media);
        contentPagerAdapter.setPositionOfMediaNews(event.getPosition());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_player_state_changed(PlayerStateEvent event) {
        switch (event.state) {
            case PLAYER_RESET:
                playerView.setVisibility(View.VISIBLE);
                playerProgress.setProgress(0);
                playerProgress.setMax(100);
                playIcon.setImageResource(R.drawable.micon_player_pause);
                break;
            case PLAYER_PLAY_ICON:
                playIcon.setImageResource(R.drawable.micon_player_play);
                break;
            case PLAYER_PAUSE_ICON:
                playIcon.setImageResource(R.drawable.micon_player_pause);
                break;
            case PLAYER_HIDE:
                playerView.setVisibility(View.GONE);
                break;
            case PLAYER_TITLE:
                playerTitle.setText(event.value);
                break;
            case PLAYER_UPDATE:
                playerStartTime.setText(event.startTime);
                playerEndTime.setText(event.endTime);
                playerProgress.setProgress(Integer.valueOf(event.value));

        }

    }

}
