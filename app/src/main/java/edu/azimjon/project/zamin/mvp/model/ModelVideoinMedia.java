package edu.azimjon.project.zamin.mvp.model;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterVideoInMedia;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelVideoinMedia {

    Retrofit retrofit;
    PresenterVideoInMedia presenterVideoInMedia;
    int offset = 1;
    String limit = "5";

    public ModelVideoinMedia(PresenterVideoInMedia presenterVideoInMedia) {
        this.presenterVideoInMedia = presenterVideoInMedia;
        offset = 1;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

    }

    public void initTopNews() {
        offset = 1;
        getVideoNews();
    }

    //TODO: Networking(getting response from server)


    //getting video news(pager news)
    public void getVideoNews() {

        retrofit.create(MyRestService.class)
                .getNewsWithType(String.valueOf(offset),
                        limit,
                        "2",
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingVideoNews(json);

                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        if (offset == 1)
                            presenterVideoInMedia.initNews(null, MESSAGE_NO_CONNECTION);
                        else
                            presenterVideoInMedia.addNews(null, MESSAGE_NO_CONNECTION);
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing top news(pager news)
    private void parsingVideoNews(JsonObject json) {
        List<SimpleNewsModel> items = ParserSimpleNewsModel.parse(json, 2);

        //sending data to view
        if (offset == 1)
            presenterVideoInMedia.initNews(items, MESSAGE_OK);
        else
            presenterVideoInMedia.addNews(items, MESSAGE_OK);

    }


}
