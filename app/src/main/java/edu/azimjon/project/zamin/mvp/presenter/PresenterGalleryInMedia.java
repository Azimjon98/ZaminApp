package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;
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
        modelGalleryInMedia.getNews();
    }

    public void getContinue() {
        modelGalleryInMedia.getNews();
    }

    public void addNews(List<NewsSimpleModel> items) {
        mainView.initGallery(items);
    }

}