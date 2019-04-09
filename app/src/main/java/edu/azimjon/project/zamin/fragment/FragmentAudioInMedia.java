package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;
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

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class FragmentAudioInMedia extends Fragment implements IFragmentAudioInMedia, MediaPlayer.OnErrorListener {

    //TODO: Constants here
    MediaPlayer mediaPlayer;


    //TODO: variables here
    WindowAudioInsideMediaBinding binding;
    View player;
    PresenterAudioInMedia presenterAudioInMedia;

    //adapters
    AudioNewsAdapter audioNewsAdapter;

    //#####################################################################

    public void setPlayer(View view) {
        player = view;
    }

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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);

        binding.listAudio.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        audioNewsAdapter.withHeader(LayoutInflater.from(getContext())
                .inflate(
                        R.layout.header_window_audio_inside_media,
                        binding.listAudio,
                        false));
        binding.listAudio.setAdapter(audioNewsAdapter);


        //*****************************************************************************

        //TODO: Init media player

        try {
            mediaPlayer.setDataSource("https://muz11.z1.fm/e/ac/via_marokand_via_marokand_-_tarnov_tarnov_2016_(zf.fm).mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setVisibility(View.GONE);

        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        //mediaPlayer prepare listener
        mediaPlayer.setOnPreparedListener(prepareListenter);
        mediaPlayer.setOnErrorListener(this);

        presenterAudioInMedia.init();
    }

    //TODO: override methods


    @Override
    public boolean getUserVisibleHint() {
        Log.d(CALLBACK_LOG, "Audio: getUserVisibleHint");

        return super.getUserVisibleHint();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d(CALLBACK_LOG, "Audio: setUserVisibleHint: " + isVisibleToUser);

        super.setUserVisibleHint(isVisibleToUser);

        if (getActivity() != null) {

            if (!isVisibleToUser) {
                player.setVisibility(View.GONE);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        }
    }


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

    //TODO: implemented methods

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        mediaPlayer.reset();
        return false;
    }

    //#################################################################

    //TODO: Argument Variables

    public MediaPlayer.OnPreparedListener prepareListenter = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
        }
    };


}