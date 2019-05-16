package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentGalleryInMedia {

    void initGallery(List<SimpleNewsModel> items, int message);
    void addGallery(List<SimpleNewsModel> items, int message);
}
