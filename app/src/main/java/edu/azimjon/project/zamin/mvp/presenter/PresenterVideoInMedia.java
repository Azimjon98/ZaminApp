package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelTopNews;
import edu.azimjon.project.zamin.mvp.model.ModelVideoinMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentVideoInMedia;

public class PresenterVideoInMedia {

    public IFragmentVideoInMedia mainView;
    ModelVideoinMedia modelVideoinMedia;

    public PresenterVideoInMedia(IFragmentVideoInMedia fragmentVideoInMedia) {
        mainView = fragmentVideoInMedia;
        modelVideoinMedia = new ModelVideoinMedia(this);

    }

    public void init() {
        modelVideoinMedia.getTopNews();
    }

    public void getContinue() {
        modelVideoinMedia.getTopNews();
    }

    public void addNews(List<NewsSimpleModel> items, int message) {
        mainView.initVideo(items, message);
    }

}