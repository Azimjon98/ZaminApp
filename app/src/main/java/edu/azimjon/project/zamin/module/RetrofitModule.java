package edu.azimjon.project.zamin.module;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;
import edu.azimjon.project.zamin.interfaces.RetrofitAnnotations;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = {OkHttpClientModule.class, ContextModule.class})
public class RetrofitModule {

    @RetrofitAnnotations.RetrofitApp
    @MyApplicationScope
    @Provides
    public Retrofit retrofit(OkHttpClient client, @RetrofitAnnotations.MyBaseUrl String baseUrl, GsonConverterFactory factory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(factory)
                .build();
    }





    @RetrofitAnnotations.MyBaseUrl
    @Provides
    String string(@ApplicationContext Context context) {
        return Constants.BASE_URL;

    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    public Gson gson() {
        return new GsonBuilder().create();
    }
}
