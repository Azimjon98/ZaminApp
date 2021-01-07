package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.fragment.FragmentAudioInMedia;
import edu.azimjon.project.zamin.fragment.FragmentGalleryInMedia;
import edu.azimjon.project.zamin.fragment.FragmentVideoInMedia;
import edu.azimjon.project.zamin.util.MyUtil;

public class MediaPagerAdapter extends FragmentPagerAdapter {

    //TODO: - Constants here:
    private String[] titles = new String[3];

    private FragmentAudioInMedia tab1;
    private FragmentVideoInMedia tab2;
    private FragmentGalleryInMedia tab3;

    Context context;
    private int tabs_number;


    public MediaPagerAdapter(Context context, FragmentManager fm, int tabs) {
        super(fm);
        this.context = context;
        this.tabs_number = tabs;

        titles[0] = MyUtil.getLocalizedString(context, R.string.tab_audio);
        titles[1] = MyUtil.getLocalizedString(context, R.string.tab_video);
        titles[2] = MyUtil.getLocalizedString(context, R.string.tab_gallery);
    }

    public void update() {
        titles[0] = MyUtil.getLocalizedString(context, R.string.tab_audio);
        titles[1] = MyUtil.getLocalizedString(context, R.string.tab_video);
        titles[2] = MyUtil.getLocalizedString(context, R.string.tab_gallery);
    }

    @Override
    public Fragment getItem(int i) {
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
        return tabs_number;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}