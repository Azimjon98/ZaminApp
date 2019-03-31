package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentMedia {

    void initAudio(List<NewsSimpleModel> items);

    void initVideo(List<NewsSimpleModel> items);

    void initGallery(List<NewsSimpleModel> items);
}
