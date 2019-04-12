package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentVideoInMedia {

    void initVideo(List<NewsSimpleModel> items, int message);
    void addVideo(List<NewsSimpleModel> items, int message);
}
