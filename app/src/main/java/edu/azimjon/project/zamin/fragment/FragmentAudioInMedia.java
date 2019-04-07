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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowAudioInsideMediaBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterAudioInMedia;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentAudioInMedia;

public class FragmentAudioInMedia extends Fragment implements IFragmentAudioInMedia {

    //TODO: Constants here


    //TODO: variables here
    WindowAudioInsideMediaBinding binding;
    PresenterAudioInMedia presenterAudioInMedia;

    //adapters
    AudioNewsAdapter audioNewsAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterAudioInMedia = new PresenterAudioInMedia(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.window_audio_inside_media, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists

        binding.listAudio.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        audioNewsAdapter.withHeader(LayoutInflater.from(getContext())
                .inflate(
                        R.layout.header_window_audio_inside_media,
                        binding.listAudio,
                        false));
        binding.listAudio.setAdapter(audioNewsAdapter);


        //*****************************************************************************


        presenterAudioInMedia.init();
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface

    @Override
    public void initAudio(List<NewsSimpleModel> items) {
        binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());

        audioNewsAdapter.init_items(items);
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################

}
