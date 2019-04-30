package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.ImageViewCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.fragment.FragmentAudioInMedia;
import edu.azimjon.project.zamin.fragment.FragmentGalleryInMedia;
import edu.azimjon.project.zamin.fragment.FragmentVideoInMedia;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class MediaPagerAdapter extends FragmentPagerAdapter {

    //TODO: - Constants here:
    String[] titles = new String[3];

//    FragmentAudioInMedia tab1;
    FragmentVideoInMedia tab2;
    FragmentGalleryInMedia tab3;

    Context context;
    private int tabs_number;


    public MediaPagerAdapter(Context context, FragmentManager fm, int tabs) {
        super(fm);
        this.context = context;
        this.tabs_number = tabs;

//        titles[0] = MyUtil.getLocalizedString(context, R.string.tab_audio);
        titles[0] = MyUtil.getLocalizedString(context, R.string.tab_video);
        titles[1] = MyUtil.getLocalizedString(context, R.string.tab_gallery);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
//            case 0:
//                if (tab1 == null)
//                    tab1 = new FragmentAudioInMedia();
//                return tab1;
            case 0:
                if (tab2 == null)
                    tab2 = new FragmentVideoInMedia();
                return tab2;
            case 1:
                if (tab3 == null)
                    tab3 = new FragmentGalleryInMedia();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabs_number;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}