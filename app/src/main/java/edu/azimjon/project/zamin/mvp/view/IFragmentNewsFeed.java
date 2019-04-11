package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentNewsFeed {

    void initMainNews(List<NewsSimpleModel> items, int messege);

    void initLastAndContinueNews(List<NewsSimpleModel> items, int messege);

    void initAudioNews(List<NewsSimpleModel> items, int messege);

    void initVideoNews(List<NewsSimpleModel> items, int messege);

    void addLastNewsContinue(List<NewsSimpleModel> items, int messege);
}
