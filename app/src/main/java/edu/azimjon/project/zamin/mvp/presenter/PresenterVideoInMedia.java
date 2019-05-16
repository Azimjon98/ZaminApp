package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelVideoinMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentVideoInMedia;

public class PresenterVideoInMedia {

    public IFragmentVideoInMedia mainView;
    ModelVideoinMedia modelVideoinMedia;

    public PresenterVideoInMedia(IFragmentVideoInMedia fragmentVideoInMedia) {
        mainView = fragmentVideoInMedia;
        modelVideoinMedia = new ModelVideoinMedia(this);

    }

    public void init() {
        modelVideoinMedia.initTopNews();
    }

    public void getContinue() {
        modelVideoinMedia.getVideoNews();
    }

    public void initNews(List<SimpleNewsModel> items, int message) {
        mainView.initVideo(items, message);
    }


    public void addNews(List<SimpleNewsModel> items, int message) {
        mainView.addVideo(items, message);
    }



}