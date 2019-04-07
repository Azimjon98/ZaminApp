package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentSearchNewsResult {

    void initNews(List<NewsSimpleModel> items);

    void addNews(List<NewsSimpleModel> items);
}