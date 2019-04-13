package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentNewsContent {

    void initContent(NewsContentModel model);

    void initLastNews(List<NewsSimpleModel> items, int message);
    void addLastNews(List<NewsSimpleModel> items, int message);
    void initTags(List<String> tags);
}
