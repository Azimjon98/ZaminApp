package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentNewsContent {

    void initContent(NewsContentModel model);

    void addLastNews(List<NewsSimpleModel> items);
    void initTags(List<String> tags);
}
