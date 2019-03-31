package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;

public class PresenterNewsFeed {

    IFragmentNewsFeed mainView;
    ModelNewsFeed modelNewsFeed;

    public PresenterNewsFeed(IFragmentNewsFeed fragmentNewsFeed) {
        mainView = fragmentNewsFeed;
        modelNewsFeed = new ModelNewsFeed(this);
    }

    public void init() {
        modelNewsFeed.getAllItems();
    }


    //TODO: callBack methods from Model to View

    public void initCategories(List<NewsCategoryModel> items) {
        mainView.initCategories(items);
    }

    public void initMainNews(List<NewsSimpleModel> items) {
        mainView.initMainNews(items);
    }

    public void initLastNews(List<NewsSimpleModel> items) {
        mainView.initLastNews(items);
    }

    public void initAudioNews(List<NewsSimpleModel> items) {
        mainView.initAudioNews(items);
    }

    public void initVideoNews(List<NewsSimpleModel> items) {
        mainView.initVideoNews(items);
    }

    //########################################################################


}
