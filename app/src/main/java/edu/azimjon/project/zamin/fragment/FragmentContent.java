package edu.azimjon.project.zamin.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.ContentPagerAdapter;
import edu.azimjon.project.zamin.addition.Constants;

public class FragmentContent extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {

    //TODO: Variables

    BottomNavigationView navigationView;
    TextView toolbarTitle;
    ViewPager contentPager;
    AppBarLayout appBarLayout;
    ImageView searchIcon;
    ImageView profileIcon;

    //TODO: initial fragments

    private int currenrItem = R.id.menu_news_feed;
    private int savedId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.window_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView = view.findViewById(R.id.bottom_navigation);
        appBarLayout = view.findViewById(R.id.app_bar);
        searchIcon = view.findViewById(R.id.toolbar_search);
        profileIcon = view.findViewById(R.id.toolbar_ic_more);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        ContentPagerAdapter contentPagerAdapter = new ContentPagerAdapter(getContext(), getFragmentManager(), 4);
        contentPager = view.findViewById(R.id.content_pager);
        contentPager.setAdapter(contentPagerAdapter);

        //when this window comes back continue with old navigation index
        if (savedId == -1) {
            navigationView.setSelectedItemId(savedId);
        }

        Log.d("myLog", "Elevation: " + appBarLayout.getElevation());
        Log.d("myLog", "transZ: " + appBarLayout.getTranslationZ());

        //declare toolbar icon touch listeners
        searchIcon.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentContent_to_fragmentSearchNews));

        profileIcon.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentContent_to_fragmentProfile));

        navigationView.setOnNavigationItemSelectedListener(this);
    }


    //TODO: override methods

    @Override
    public void onStop() {
        super.onStop();

        Log.d(Constants.MY_LOG, "onSaveInstanceState: " + navigationView.getSelectedItemId());
        savedId = navigationView.getSelectedItemId();
    }


    //#################################################################


    //TODO: all methods from interface

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        toolbarTitle.setText(item.getTitle());
        switch (item.getItemId()) {
            case R.id.menu_news_feed:
                contentPager.setCurrentItem(0);
                appBarLayout.setElevation(4f);
                return true;
            case R.id.menu_topnews:
                contentPager.setCurrentItem(1);
                appBarLayout.setElevation(4f);
                return true;
            case R.id.menu_favourites:
                contentPager.setCurrentItem(2);
                appBarLayout.setElevation(4f);
                return true;
            case R.id.menu_media:
                contentPager.setCurrentItem(3);
                appBarLayout.setElevation(0f);
                return true;
        }
        return false;
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################

}
