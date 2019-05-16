package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentMedia {

    void initAudio(List<SimpleNewsModel> items);

    void initVideo(List<SimpleNewsModel> items);

    void initGallery(List<SimpleNewsModel> items);
}
