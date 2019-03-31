package edu.azimjon.project.zamin.mvp.model;

import java.util.Arrays;

import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsFeed;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;

public class ModelTopNews {

    PresenterTopNews presenterTopNews;

    public ModelTopNews(PresenterTopNews presenterTopNews){
        this.presenterTopNews = presenterTopNews;
    }

    public void getAllItems(){
        presenterTopNews.initNews(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));


    }

}
