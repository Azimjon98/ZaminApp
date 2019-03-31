package edu.azimjon.project.zamin.mvp.model;

import java.util.Arrays;

import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterMedia;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;

public class ModelMedia {

    PresenterMedia presenterMedia;

    public ModelMedia(PresenterMedia presenterMedia){
        this.presenterMedia = presenterMedia;
    }

    public void getAllItems(){
        presenterMedia.initAudio(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

        presenterMedia.initVideo(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));

        presenterMedia.initGallery(Arrays.asList(new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel(),
                new NewsSimpleModel()));


    }

}
