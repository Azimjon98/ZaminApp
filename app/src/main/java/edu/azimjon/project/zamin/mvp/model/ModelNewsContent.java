package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.parser.ParserNewsContentModel;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelNewsContent {

    ParserSimpleNewsModel parserSimpleNewsModel;

    Retrofit retrofit;
    PresenterNewsContent presenterNewsContent;

    String limit = "4";
    int offset;

    public ModelNewsContent(PresenterNewsContent presenterNewsContent) {
        offset = 1;

        this.presenterNewsContent = presenterNewsContent;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

        parserSimpleNewsModel = new ParserSimpleNewsModel();
    }

    public void getAllItems(String newsId) {
        getContent(newsId);
        getLastNews();
        getTags(newsId);

    }

    //TODO: Networking(getting response from server)

    //getting main news(pager news)
    private void getContent(String newsId) {

        retrofit.create(MyRestService.class)
                .getNewsContentWithId(String.valueOf(newsId),
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingNewsContent(json);


                        } else {
                            Log.d(API_LOG, "getMainNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getMainNews onFailure: " + t.getMessage());
                    }
                });
    }

    //getting main news(pager news)
    public void getLastNews() {

        retrofit.create(MyRestService.class)
                .getLastNewsData(String.valueOf(offset),
                        limit,
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            if (offset == 1)
                                parsingInitLastNews(json);
                            else
                                parsingAddLastNews(json);

                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());

                        if (offset == 1)
                            presenterNewsContent.initLastNews(null, MESSAGE_NO_CONNECTION);
                        else
                            presenterNewsContent.addLastNews(null, MESSAGE_NO_CONNECTION);
                    }
                });
    }

    //getting main news(pager news)
    public void getTags(String id) {

        retrofit.create(MyRestService.class)
                .getNewsContentTags(
                        id,
                        MySettings.getInstance().getLang())
                .enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.isSuccessful()) {
                            JsonArray json = response.body();
                            parsingTags(json);

                        } else {
                            Log.d(API_LOG, "getTags onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.d(API_LOG, "getTags onFailure: " + t.getMessage());
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing main news(pager news)
    private void parsingNewsContent(JsonObject json) {
        NewsContentModel model = new ParserNewsContentModel().parse(json);

        //sending data to view
        presenterNewsContent.initContent(model);

    }

    //parsing main news(pager news)

    private void parsingInitLastNews(JsonObject json) {
        List<NewsSimpleModel> items = parserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsContent.initLastNews(items, MESSAGE_OK);

    }

    private void parsingAddLastNews(JsonObject json) {
        List<NewsSimpleModel> items = parserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsContent.addLastNews(items, MESSAGE_OK);

    }

    //parsing main news(pager news)
    private void parsingTags(JsonArray json) {
        List<String> tags = new ArrayList<>();


        for (JsonElement i : json) {
            JsonObject item = i.getAsJsonObject();

            tags.add(item.getAsJsonPrimitive("tag").getAsString());
        }


        //sending data to view
        if (tags.size() != 0)
            presenterNewsContent.initTags(tags);

    }
}