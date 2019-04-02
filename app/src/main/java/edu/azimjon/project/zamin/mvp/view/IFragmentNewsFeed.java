package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentNewsFeed {

    void initCategories(List<NewsCategoryModel> items);

    void initMainNews(List<NewsSimpleModel> items);

    void initLastNews(List<NewsSimpleModel> items);

    void initAudioNews(List<NewsSimpleModel> items);

    void initVideoNews(List<NewsSimpleModel> items);

    void addLastNewsContinue(List<NewsSimpleModel> items);
}
