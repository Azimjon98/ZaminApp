package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentTopNews {

    void initNews(List<NewsSimpleModel> items, int message);

    void addNews(List<NewsSimpleModel> items, int message);


}
