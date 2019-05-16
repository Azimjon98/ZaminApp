package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentVideoInMedia {

    void initVideo(List<SimpleNewsModel> items, int message);
    void addVideo(List<SimpleNewsModel> items, int message);
}
