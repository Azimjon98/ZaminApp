package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.fragment.FragmentTopNews;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

public class PresenterTopNews {

    IFragmentTopNews mainView;

    public PresenterTopNews(IFragmentTopNews fragmentNewsFeed) {
        mainView = fragmentNewsFeed;
    }

    public void init() {
        ModelTopNews modelTopNews = new ModelTopNews(this);
        modelTopNews.getAllItems();

    }


    public void initNews(List<NewsSimpleModel> items) {
        mainView.initNews(items);

    }




}
