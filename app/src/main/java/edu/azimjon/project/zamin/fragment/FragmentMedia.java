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

//public class FragmentMedia extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{
//    //TODO: Variables
//    FragmentManager manager;
//
//    BottomNavigationView navigationView;
//    TabLayout tabMedia;
//
//    //TODO: initial fragments
//
//    FragmentNewsFeed fragmentNewsFeed;
//    FragmentTopNews fragmentTopNews;
//    FragmentFavourites fragmentFavourites;
//    FragmentMedia fragmentMedia;
//
//    private int currenrItem = R.id.menu_news_feed;
//    private int savedId = -1;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.window_content, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        manager = getChildFragmentManager();
//        navigationView = view.findViewById(R.id.bottom_navigation);
//        tabMedia = view.findViewById(R.id.tab_media);
//
//        navigationView.setOnNavigationItemSelectedListener(this);
//
//
//        if (savedId == -1) {
//            fragmentNewsFeed = new FragmentNewsFeed();
//            manager.beginTransaction()
//                    .replace(R.id.fr_container, fragmentNewsFeed, fragmentNewsFeed.getClass().getSimpleName().toString())
//                    .commit();
//
//        } else {
//            switch (savedId) {
//                case R.id.menu_news_feed:
//                    fragmentNewsFeed = new FragmentNewsFeed();
//                    manager.beginTransaction()
//                            .replace(R.id.fr_container, fragmentNewsFeed, fragmentNewsFeed.getClass().getSimpleName().toString())
//                            .commit();
//                    break;
//                case R.id.menu_topnews:
//                    fragmentTopNews = new FragmentTopNews();
//                    manager.beginTransaction()
//                            .replace(R.id.fr_container, fragmentTopNews, fragmentTopNews.getClass().getSimpleName().toString())
//                            .commit();
//                    break;
//                case R.id.menu_favourites:
//                    fragmentFavourites = new FragmentFavourites();
//                    manager.beginTransaction()
//                            .replace(R.id.fr_container, fragmentFavourites, fragmentFavourites.getClass().getSimpleName().toString())
//                            .commit();
//                    break;
//                case R.id.menu_media:
//                    createFragmentMedia();
//                    manager.beginTransaction()
//                            .replace(R.id.fr_container, fragmentMedia, fragmentMedia.getClass().getSimpleName().toString())
//                            .commit();
//                    break;
//            }
//
//        }
//
//    }
//
//
//    //TODO: override methods
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        Log.d(Constants.MY_LOG, "onSaveInstanceState: " + navigationView.getSelectedItemId());
//        savedId = navigationView.getSelectedItemId();
//    }
//
//
//    //#################################################################
//
//
//    //TODO: all methods from interface
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int temp = currenrItem;
//        currenrItem = menuItem.getItemId();
//
//
//        return change_pager(menuItem.getItemId(), temp);
//    }
//
//
//    //#################################################################
//
//    //TODO: Additional methods
//
//    private void createFragmentMedia(){
//        fragmentMedia = new FragmentMedia();
////        fragmentMedia.setTabLayout(tabMedia);
////        fragmentMedia.setFragmentManager(manager);
//    }
//
//    //solve is windwow will change and to which
//    private boolean change_pager(int id, int temp) {
//        //prepare(hide) tab before transacting
//        tabMedia.setVisibility(View.GONE);
//
//        switch (id) {
//            case R.id.menu_news_feed:
//                if (temp != R.id.menu_news_feed) {
//                    transact_with_animation(temp, id, fragmentNewsFeed);
//                }
//                return true;
//            case R.id.menu_topnews:
//                if (temp != R.id.menu_topnews) {
//                    if (fragmentTopNews == null)
//                        fragmentTopNews = new FragmentTopNews();
//                    transact_with_animation(temp, id, fragmentTopNews);
//                }
//                return true;
//            case R.id.menu_favourites:
//                if (temp != R.id.menu_favourites) {
//                    if (fragmentFavourites == null) {
//                        fragmentFavourites = new FragmentFavourites();
//                    }
//                    transact_with_animation(temp, id, fragmentFavourites);
//                }
//                return true;
//            case R.id.menu_media:
//                if (temp != R.id.menu_media) {
//                    if (fragmentMedia == null) {
//                        createFragmentMedia();
//                    }
//                    //show tabLayout with delay to give time to animation
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            tabMedia.setVisibility(View.VISIBLE);
//
//                        }
//                    },300);
//
//                    transact_with_animation(temp, id, fragmentMedia);
//                }
//                return true;
//        }
//        return false;
//    }
//
//
//    //Transaction method with simple animation
//    void transact_with_animation(int from_view, int to_view, Fragment fragment) {
//        if (get_position(from_view) < get_position(to_view)) {
//            manager.beginTransaction()
//                    .setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left)
//                    .replace(R.id.fr_container, fragment, fragment.getClass().getSimpleName().toString())
//                    .commit();
//
//            Log.d(Constants.CALLBACK_LOG, "transact: " + fragment.getClass().getSimpleName().toString());
//        } else {
//            manager.beginTransaction()
//                    .setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right)
//                    .replace(R.id.fr_container, fragment, fragment.getClass().getSimpleName().toString())
//                    .commit();
//
//            Log.d(Constants.CALLBACK_LOG, "transact: " + fragment.getClass().getSimpleName().toString());
//        }
//
//    }
//
//    private int get_position(int item) {
//        switch (item) {
//            case R.id.menu_news_feed:
//                return 1;
//            case R.id.menu_topnews:
//                return 2;
//            case R.id.menu_favourites:
//                return 3;
//            case R.id.menu_media:
//                return 4;
//        }
//        return 1;
//    }
//
//
//    //#################################################################
//
//}

public class FragmentMedia extends Fragment implements IFragmentMedia, ViewPager.OnPageChangeListener {

    //TODO: Constants here


    //TODO: BaseClass views
    TabLayout tabLayout;
    FragmentManager fragmentManager;

    //TODO: variables here
    WindowMediaBinding binding;
    PresenterMedia presenterMedia;

    //adapters
    MediaPagerAdapter pagerAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(CALLBACK_LOG,"FragmentMedia: onCreate");

        presenterMedia = new PresenterMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(CALLBACK_LOG,"FragmentMedia: onCreateView");
        binding = DataBindingUtil.inflate(inflater, R.layout.window_media, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(CALLBACK_LOG,"FragmentMedia: onViewCreated");


        //initialize adapters and append to lists or pagers

        pagerAdapter = new MediaPagerAdapter(getContext(), getChildFragmentManager() ,3);
        binding.mediaPager.setAdapter(pagerAdapter);
        binding.mediaPager.addOnPageChangeListener(this);
       // binding.mediaPager.setCurrentItem(1);

        if (tabLayout != null){
            tabLayout.setupWithViewPager(binding.mediaPager, true);

            //select Video Item when opening
//            tabLayout.getTabAt(1).select();
        }

//        binding..setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
//        binding.listTopNews.setAdapter(mediumNewsAdapter);


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

    public void setFragmentManager(FragmentManager manager){
        fragmentManager = manager;

    }


    //#################################################################


}
