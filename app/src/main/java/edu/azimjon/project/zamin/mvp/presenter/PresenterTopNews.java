package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

public class PresenterTopNews {

    public IFragmentTopNews mainView;
    ModelTopNews modelTopNews;

    public PresenterTopNews(IFragmentTopNews fragmentNewsFeed) {
        mainView = fragmentNewsFeed;
        modelTopNews = new ModelTopNews(this);

    }

    public void init() {
        modelTopNews.getTopNews(true);
    }

    public void getContinue() {
        modelTopNews.getTopNews(false);
    }

    public void initNews(List<SimpleNewsModel> items, int message) {
        mainView.initNews(items, message);
    }

    public void addNews(List<SimpleNewsModel> items, int message) {
        mainView.addNews(items, message);
    }


}