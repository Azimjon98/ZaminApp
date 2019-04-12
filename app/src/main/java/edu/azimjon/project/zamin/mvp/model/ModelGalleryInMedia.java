package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterGalleryInMedia;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;

public class ModelGalleryInMedia {

    ParserSimpleNewsModel parserSimpleNewsModel;

    Retrofit retrofit;
    PresenterGalleryInMedia presenterGalleryInMedia;
    int offset = 1;
    String limit = "10";

    public ModelGalleryInMedia(PresenterGalleryInMedia presenterGalleryInMedia) {
        this.presenterGalleryInMedia = presenterGalleryInMedia;
        offset = 1;

        try {
            retrofit = MyApplication.getInstance().getMyApplicationComponent().getRetrofitApp();
        } catch (ClassNotFoundException e) {
            System.out.println("myError : " + e);
            e.printStackTrace();
        }

        parserSimpleNewsModel = new ParserSimpleNewsModel(((Fragment) presenterGalleryInMedia.mainView));
    }


    //TODO: Networking(getting response from server)


    //getting main news(pager news)
    public void getNews() {

        //declare data get here
    }


    //TODO: Parsing and sending data to our view

    //parsing top news(pager news)
    private void parsingTopNews(JsonObject json) {
        List<NewsSimpleModel> items = new ArrayList<>();

        //sending data to view
        presenterGalleryInMedia.addNews(items, MESSAGE_OK);

    }


}
