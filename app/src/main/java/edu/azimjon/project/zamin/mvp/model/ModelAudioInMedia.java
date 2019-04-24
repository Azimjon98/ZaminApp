package edu.azimjon.project.zamin.mvp.model;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterAudioInMedia;
import edu.azimjon.project.zamin.parser.ParserMediaNewsModel;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_ITEMS;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelAudioInMedia {

    ParserMediaNewsModel parserMediaNewsModel;

    Retrofit retrofit;
    PresenterAudioInMedia presenterAudioInMedia;
    int offset = 1;
    String limit = "5";

    public ModelAudioInMedia(PresenterAudioInMedia presenterAudioInMedia) {
        this.presenterAudioInMedia = presenterAudioInMedia;
        offset = 1;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

    }

    public void initAudioNews() {
        offset = 1;
        getAudioNews();
    }


    //TODO: Networking(getting response from server)


    //getting audio news()
    public void getAudioNews() {
        parserMediaNewsModel = new ParserMediaNewsModel();

        retrofit.create(MyRestService.class)
                .getNewsWithType(String.valueOf(offset),
                        limit,
                        "3",
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingAudioNews(json);


                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        if (offset == 1)
                            presenterAudioInMedia.initNews(null, MESSAGE_NO_CONNECTION);
                        else
                            presenterAudioInMedia.addNews(null, MESSAGE_NO_CONNECTION);
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing top news(pager news)
    private void parsingAudioNews(JsonObject json) {
        List<MediaNewsModel> items = parserMediaNewsModel.parse(json, 3);

        //sending data to view
        if (items.size() == 0) {
            if (offset == 1)
                presenterAudioInMedia.initNews(items, MESSAGE_NO_ITEMS);
            else
                presenterAudioInMedia.addNews(items, MESSAGE_NO_ITEMS);
        } else {
            if (offset == 1)
                presenterAudioInMedia.initNews(items, MESSAGE_OK);
            else
                presenterAudioInMedia.addNews(items, MESSAGE_OK);
        }


    }


}
