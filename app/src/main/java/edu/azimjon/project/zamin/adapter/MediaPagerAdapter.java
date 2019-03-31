package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import edu.azimjon.project.zamin.fragment.FragmentAudioInMedia;
import edu.azimjon.project.zamin.fragment.FragmentGalleryInMedia;
import edu.azimjon.project.zamin.fragment.FragmentVideoInMedia;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class MediaPagerAdapter extends FragmentPagerAdapter {

    //TODO: - Constants here:
    String[] titles = {"Audio", "Video", "Galerey"};

    FragmentAudioInMedia tab1;
    FragmentVideoInMedia tab2;
    FragmentGalleryInMedia tab3;

    Context context;
    private int tabs_number;

    public MediaPagerAdapter(Context context, FragmentManager fm, int tabs) {
        super(fm);
        this.context = context;
        this.tabs_number = tabs;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(CALLBACK_LOG, "MediaPagerAdapter: getItem");
        switch (i) {
            case 0:
                if (tab1 == null)
                    tab1 = new FragmentAudioInMedia();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new FragmentVideoInMedia();
                return tab2;
            case 2:
                if (tab3 == null)
                    tab3 = new FragmentGalleryInMedia();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        Log.d(CALLBACK_LOG, "getCount: " + tabs_number);

        return tabs_number;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}