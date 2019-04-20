package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelNewsFeed;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsFeed;

public class PresenterNewsFeed {

    public IFragmentNewsFeed mainView;
    ModelNewsFeed modelNewsFeed;

    public PresenterNewsFeed(IFragmentNewsFeed fragmentNewsFeed) {
        mainView = fragmentNewsFeed;
        modelNewsFeed = new ModelNewsFeed(this);
    }

    public void init() {
        modelNewsFeed.getAllItems();
    }

    public void getLastNewsContinue() {
        modelNewsFeed.getlLastNewsContinue();
    }


    //TODO: callBack methods from Model to View

    //cannot use because of liveData in view

    public void initMainNews(List<NewsSimpleModel> items, int message) {
        mainView.initMainNews(items, message);
    }

    public void initLastNews(List<NewsSimpleModel> items, int message) {
        mainView.initLastAndContinueNews(items, message);
    }

    public void initAudioNews(List<MediaNewsModel> items, int message) {
        mainView.initAudioNews(items, message);
    }

    public void initVideoNews(List<MediaNewsModel> items, int message) {
        mainView.initVideoNews(items, message);
    }

    public void addLastNewsContinue(List<NewsSimpleModel> items, int message) {
        mainView.addLastNewsContinue(items, message);
    }

    //########################################################################


}
