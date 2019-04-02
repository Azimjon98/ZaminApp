package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelFavouriteNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentFavouriteNews;

public class PresenterFavouriteNews {

    IFragmentFavouriteNews mainView;

    public PresenterFavouriteNews(IFragmentFavouriteNews fragmentFavouriteNews) {
        mainView = fragmentFavouriteNews;
    }

    public void init() {
        ModelFavouriteNews modelFavouriteNews = new ModelFavouriteNews(this);
        modelFavouriteNews.getAllItems();
    }

    public void initFavourites(List<FavouriteNewsModel> items) {
        mainView.initFavourites(items);

    }


}
