package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentVideoInMedia {

    void initVideo(List<MediaNewsModel> items, int message);
    void addVideo(List<MediaNewsModel> items, int message);
}
