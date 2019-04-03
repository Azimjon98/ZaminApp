package edu.azimjon.project.zamin.mvp.model;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterAudioInMedia;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.parser.ParserSimpleNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;

public class ModelAudioInMedia {

    ParserSimpleNewsModel parserSimpleNewsModel;

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

        parserSimpleNewsModel = new ParserSimpleNewsModel(((Fragment)presenterAudioInMedia.mainView));
    }


    //TODO: Networking(getting response from server)


    //getting main news(pager news)
    public void getAudioNews() {
        presenterAudioInMedia.addNews(Arrays.asList(
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()
        ));

        //declare getting data method here

    }


    //TODO: Parsing and sending data to our view

    //parsing top news(pager news)
    private void parsingTopNews(JsonObject json) {
        List<NewsSimpleModel> items = new ArrayList<>();

        //sending data to view
        presenterAudioInMedia.addNews(items);

    }



}