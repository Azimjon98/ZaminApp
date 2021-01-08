package edu.azimjon.project.zamin.component;

import android.media.MediaPlayer;

import com.bumptech.glide.RequestManager;

import dagger.Component;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;
import edu.azimjon.project.zamin.interfaces.RetrofitAnnotations;
import edu.azimjon.project.zamin.module.GlideModule;
import edu.azimjon.project.zamin.module.MediaPlayerModule;
import edu.azimjon.project.zamin.module.RetrofitModule;
import retrofit2.Retrofit;

@MyApplicationScope
@Component(modules = {RetrofitModule.class, GlideModule.class, MediaPlayerModule.class})
public interface MyApplicationComponent {

    @RetrofitAnnotations.RetrofitApp
    Retrofit getRetrofitApp();

    RequestManager getGlideManager();

    MediaPlayer getMediaPlayer();
}
