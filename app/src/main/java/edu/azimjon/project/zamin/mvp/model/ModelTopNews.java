package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelTopNews {

    ParserSimpleNewsModel parserSimpleNewsModel;

    Retrofit retrofit;
    PresenterTopNews presenterTopNews;
    int offset = 1;
    String limit = "10";

    public ModelTopNews(PresenterTopNews presenterTopNews) {
        this.presenterTopNews = presenterTopNews;
        offset = 1;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

        parserSimpleNewsModel = new ParserSimpleNewsModel(((Fragment) presenterTopNews.mainView));
    }


    //TODO: Networking(getting response from server)

    //getting main news(pager news)
    public void initTopNews() {
        offset = 1;

        retrofit.create(MyRestService.class)
                .getTopNews(String.valueOf(offset),
                        limit,
                        "1",
                        "uz")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingInitTopNews(json);


                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        presenterTopNews.initNews(null, MESSAGE_NO_CONNECTION);

                    }
                });
    }

    //getting main news(pager news)
    public void getTopNews() {

        retrofit.create(MyRestService.class)
                .getTopNews(String.valueOf(offset),
                        limit,
                        "1",
                        "uz")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingTopNews(json);


                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                        presenterTopNews.addNews(null, MESSAGE_NO_CONNECTION);

                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing top news(pager news)
    private void parsingInitTopNews(JsonObject json) {
        List<NewsSimpleModel> items = parserSimpleNewsModel.parse(json);

        //sending data to view
        presenterTopNews.initNews(items, MESSAGE_OK);

    }

    //parsing top news(pager news)
    private void parsingTopNews(JsonObject json) {
        List<NewsSimpleModel> items = parserSimpleNewsModel.parse(json);

        //sending data to view
        presenterTopNews.addNews(items, MESSAGE_OK);

    }


}
