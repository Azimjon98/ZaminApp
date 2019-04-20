package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public interface IFragmentGalleryInMedia {

    void initGallery(List<MediaNewsModel> items, int message);
    void addGallery(List<MediaNewsModel> items, int message);
}
