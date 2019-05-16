package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentTopNews {

    void initNews(List<SimpleNewsModel> items, int message);

    void addNews(List<SimpleNewsModel> items, int message);


}
