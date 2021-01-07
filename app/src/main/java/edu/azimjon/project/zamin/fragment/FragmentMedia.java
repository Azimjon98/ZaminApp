package edu.azimjon.project.zamin.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediaPagerAdapter;
import edu.azimjon.project.zamin.databinding.WindowMediaBinding;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

public class FragmentMedia extends Fragment {

    //TODO: Constants here


    //TODO: BaseClass views

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
        if (pagerAdapter == null) {
            pagerAdapter = new MediaPagerAdapter(getContext(), getChildFragmentManager(), 3);
            binding.mediaPager.setAdapter(pagerAdapter);
//        binding.mediaPager.addOnPageChangeListener(this);
            binding.tabMedia.setupWithViewPager(binding.mediaPager);
            binding.mediaPager.setOffscreenPageLimit(1);
            binding.mediaPager.setCurrentItem(position);
        }else{
            String titles0 = MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.tab_video);
            String titles1 = MyUtil.getLocalizedString(getContext(), R.string.tab_gallery);
            Objects.requireNonNull(binding.tabMedia.getTabAt(0)).setText(titles0);
            Objects.requireNonNull(binding.tabMedia.getTabAt(1)).setText(titles1);
//            pagerAdapter.notifyDataSetChanged();
        }


        //*****************************************************************************

        //transfarmation in viewPager
        binding.mediaPager.setPageTransformer(true, zoomOutTransformation);

    }

    //TODO: override methods


    @Override
    public void onResume() {
        super.onResume();
        //Trying to open tab item when navigationg from newsFeed
        if (binding != null && binding.tabMedia != null) {
            try {
                binding.tabMedia.getTabAt(position).select();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(ERROR_LOG, e.getMessage());
            }
        }

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
