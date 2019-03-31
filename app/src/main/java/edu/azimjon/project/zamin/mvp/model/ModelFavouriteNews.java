package edu.azimjon.project.zamin.mvp.model;

import java.util.Arrays;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterFavouriteNews;

public class ModelFavouriteNews {

    PresenterFavouriteNews presenterFavouriteNews;

    public ModelFavouriteNews(PresenterFavouriteNews presenterFavouriteNews){
        this.presenterFavouriteNews = presenterFavouriteNews;
    }

    public void getAllItems(){
        presenterFavouriteNews.initFavourites(Arrays.asList(new FavouriteNewsModel(),
                new FavouriteNewsModel(),
                new FavouriteNewsModel(),
                new FavouriteNewsModel(),
                new FavouriteNewsModel()));

    }

}
