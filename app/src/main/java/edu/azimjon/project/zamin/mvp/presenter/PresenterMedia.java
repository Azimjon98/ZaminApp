package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelMedia;
import edu.azimjon.project.zamin.mvp.model.ModelTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

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

    public void initAudio(List<NewsSimpleModel> items){
        mainView.initAudio(items);
    }

    public void initVideo(List<NewsSimpleModel> items){
        mainView.initVideo(items);
    }

    public void initGallery(List<NewsSimpleModel> items){
        mainView.initGallery(items);
    }


    //########################################################################



}
