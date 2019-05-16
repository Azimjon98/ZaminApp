package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.ContentNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentNewsContent {

    void initContent(ContentNewsModel model, int message);

    void initLastNews(List<SimpleNewsModel> items, int message);
    void addLastNews(List<SimpleNewsModel> items, int message);
    void initTags(List<String> tags);
}
