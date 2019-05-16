package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
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

    public void initCategories(List<CategoryNewsModel> items, int message) {
        mainView.initCategories(items, message);
    }

    public void initMainNews(List<SimpleNewsModel> items, int message) {
        mainView.initMainNews(items, message);
    }

    public void initLastNews(List<SimpleNewsModel> items, int message) {
        mainView.initLastAndContinueNews(items, message);
    }

    public void initAudioNews(List<SimpleNewsModel> items, int message) {
        mainView.initAudioNews(items, message);
    }

    public void initVideoNews(List<SimpleNewsModel> items, int message) {
        mainView.initVideoNews(items, message);
    }

    public void addLastNewsContinue(List<SimpleNewsModel> items, int message) {
        mainView.addLastNewsContinue(items, message);
    }

    //########################################################################


}
