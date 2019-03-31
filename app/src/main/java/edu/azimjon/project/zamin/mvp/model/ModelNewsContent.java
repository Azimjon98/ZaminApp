package edu.azimjon.project.zamin.mvp.model;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.Arrays;
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

    Retrofit retrofit;
    PresenterNewsContent presenterNewsContent;

    public ModelNewsContent(PresenterNewsContent presenterNewsContent) {

        this.presenterNewsContent = presenterNewsContent;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }
    }

    public void getAllItems(String newsId) {

        getContent(newsId);
        getLastNews();

        System.out.println(retrofit != null);

        presenterNewsContent.initLastNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));


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
    private void getLastNews() {

        retrofit.create(MyRestService.class)
                .getNewsData(String.valueOf("1"),
                        "4",
                        "uz")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            JsonObject json = response.body();
                            parsingLastNews(json);


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
        List<NewsSimpleModel> items = ParserSimpleNewsModel.parse(json);

        //sending data to view
        presenterNewsContent.initLastNews(items);

    }


}
