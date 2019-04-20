package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentAudioInMedia {

    void initAudio(List<MediaNewsModel> items, int message);
    void addAudio(List<MediaNewsModel> items, int message);
}
