package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

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

        parserSimpleNewsModel = new ParserSimpleNewsModel(((Fragment) presenterNewsContent.mainView));
    }

    public void getAllItems(String newsId) {
        getContent(newsId);
        getLastNews();

    }

    //TODO: Networking(getting response from server)

    //getting main news(pager news)
    private void getContent(String newsId) {

        retrofit.create(MyRestService.class)
                .getNewsContentWithId(String.valueOf(newsId),
                        "uz")
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
                .getNewsData(String.valueOf(offset),
                        limit,
                        "uz")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingLastNews(json);

                            offset++;
                        } else {
                            Log.d(API_LOG, "getLastNews onFailure: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(API_LOG, "getLastNews onFailure: " + t.getMessage());
                    }
                });
    }


    //TODO: Parsing and sending data to our view

    //parsing main news(pager news)
    private void parsingNewsContent(JsonObject json) {
        NewsContentModel model = ParserNewsContentModel.parse(json);

        //sending data to view
        presenterNewsContent.initContent(model);

    }

    //parsing main news(pager news)
    private void parsingLastNews(JsonObject json) {
        List<NewsSimpleModel> items = parserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsContent.addLastNews(items);

    }
}