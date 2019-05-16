package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelGalleryInMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentGalleryInMedia;

public class PresenterGalleryInMedia {

    public IFragmentGalleryInMedia mainView;
    ModelGalleryInMedia modelGalleryInMedia;

    public PresenterGalleryInMedia(IFragmentGalleryInMedia fragmentVideoInMedia) {
        mainView = fragmentVideoInMedia;
        modelGalleryInMedia = new ModelGalleryInMedia(this);

    }

    public void init() {
        modelGalleryInMedia.initGalleryNews();
    }

    public void getContinue() {
        modelGalleryInMedia.getGalleryNews();
    }

    public void initNews(List<SimpleNewsModel> items, int message) {
        mainView.initGallery(items, message);
    }

    public void addNews(List<SimpleNewsModel> items, int message) {
        mainView.addGallery(items, message);
    }

}