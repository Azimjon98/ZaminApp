package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentMedia;

public class PresenterMedia {

    IFragmentMedia mainView;

    public PresenterMedia(IFragmentMedia fragmentMedia) {
        mainView = fragmentMedia;
    }

    public void init() {
        ModelMedia modelMedia = new ModelMedia(this);
        modelMedia.getAllItems();

    }




    //TODO: callBack methods from Model to View

    public void initAudio(List<SimpleNewsModel> items){
        mainView.initAudio(items);
    }

    public void initVideo(List<SimpleNewsModel> items){
        mainView.initVideo(items);
    }

    public void initGallery(List<SimpleNewsModel> items){
        mainView.initGallery(items);
    }


    //########################################################################



}
