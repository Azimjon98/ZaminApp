package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.azimjon.project.zamin.fragment.FragmentFavourites;
import edu.azimjon.project.zamin.fragment.FragmentMedia;
import edu.azimjon.project.zamin.fragment.FragmentNewsFeed;
import edu.azimjon.project.zamin.fragment.FragmentTopNews;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class ContentPagerAdapter extends FragmentPagerAdapter {

    //TODO: - Constants here:

    FragmentNewsFeed fragmentNewsFeed;
    FragmentTopNews fragmentTopNews;
    FragmentFavourites fragmentFavourites;
    FragmentMedia fragmentMedia;

    Context context;
    private int tabs_number;

    public ContentPagerAdapter(Context context, FragmentManager fm, int tabs) {
        super(fm);
        Log.d(CALLBACK_LOG, "ContentPagerAdapter: init");
        this.context = context;
        this.tabs_number = tabs;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                if (fragmentNewsFeed == null)
                    fragmentNewsFeed = new FragmentNewsFeed();
                return fragmentNewsFeed;
            case 1:
                if (fragmentTopNews == null)
                    fragmentTopNews = new FragmentTopNews();
                return fragmentTopNews;
            case 2:
                if (fragmentFavourites == null)
                    fragmentFavourites = new FragmentFavourites();
                return fragmentFavourites;
            case 3:
                if (fragmentMedia == null)
                    fragmentMedia = new FragmentMedia();

                return fragmentMedia;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return tabs_number;
    }


    //additional inside methods

    public void setPositionOfMediaNews(int positionOfMediaNews) {
        if (fragmentMedia == null)
            fragmentMedia = new FragmentMedia();
        fragmentMedia.setPosition(positionOfMediaNews);
    }

}