package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.databinding.WindowAudioInsideMediaBinding;
import edu.azimjon.project.zamin.databinding.WindowVideoInsideMediaBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterVideoInMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentVideoInMedia;

public class FragmentVideoInMedia extends Fragment implements IFragmentVideoInMedia {

    //TODO: Constants here


    //TODO: variables here
    WindowVideoInsideMediaBinding binding;
    PresenterVideoInMedia presenterVideoInMedia;

    //adapters
    VideoNewsAdapter videoNewsAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterVideoInMedia = new PresenterVideoInMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_video_inside_media, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists

        binding.listVideo.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        videoNewsAdapter = new VideoNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listVideo.setAdapter(videoNewsAdapter);


        //*****************************************************************************

        presenterVideoInMedia.init();

    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initVideo(List<NewsSimpleModel> items) {
        videoNewsAdapter.init_items(items);
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################

}
