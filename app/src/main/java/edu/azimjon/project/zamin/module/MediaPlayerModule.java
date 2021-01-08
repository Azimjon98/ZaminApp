package edu.azimjon.project.zamin.module;

import android.media.AudioManager;
import android.media.MediaPlayer;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;

@Module
public class MediaPlayerModule {

    @MyApplicationScope
    @Provides
    public MediaPlayer provideMediaPlayer(){
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setScreenOnWhilePlaying(true);

        return mediaPlayer;
    }
}
