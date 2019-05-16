package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.ContentNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.model.ModelNewsContent;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;

public class PresenterNewsContent {

    public IFragmentNewsContent mainView;
    ModelNewsContent modelNewsContent;

    public PresenterNewsContent(IFragmentNewsContent fragmentNewsContent) {
        mainView = fragmentNewsContent;
        modelNewsContent = new ModelNewsContent(this);
    }

    public void init(String newsId) {
        modelNewsContent.getAllItems(newsId);
    }

    public void getContinue() {
        modelNewsContent.getLastNews(false);
    }


    //TODO: callBack methods from Model to View

    public void initContent(ContentNewsModel model, int message) {
        mainView.initContent(model, message);
    }

    public void initLastNews(List<SimpleNewsModel> items, int message) {
        mainView.initLastNews(items, message);
    }

    public void addLastNews(List<SimpleNewsModel> items, int message) {
        mainView.addLastNews(items, message);
    }

    public void initTags(List<String> tags) {
        mainView.initTags(tags);
    }


    //########################################################################


}
