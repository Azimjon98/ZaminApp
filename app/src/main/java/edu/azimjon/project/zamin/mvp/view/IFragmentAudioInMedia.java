package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentAudioInMedia {

    void initAudio(List<SimpleNewsModel> items, int message);
    void addAudio(List<SimpleNewsModel> items, int message);
}
