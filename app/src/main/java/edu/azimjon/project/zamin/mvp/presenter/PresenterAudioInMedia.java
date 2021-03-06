package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelAudioInMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentAudioInMedia;

public class PresenterAudioInMedia {

    public IFragmentAudioInMedia mainView;
    ModelAudioInMedia modelAudioInMedia;

    public PresenterAudioInMedia(IFragmentAudioInMedia fragmentAudioInMedia) {
        mainView = fragmentAudioInMedia;
        modelAudioInMedia = new ModelAudioInMedia(this);

    }

    public void init() {
        modelAudioInMedia.initAudioNews();
    }

    public void getContinue() {
        modelAudioInMedia.getAudioNews();
    }

    public void initNews(List<SimpleNewsModel> items, int message) {
        mainView.initAudio(items, message);
    }

    public void addNews(List<SimpleNewsModel> items, int message) {
        mainView.addAudio(items, message);
    }


}