package edu.azimjon.project.zamin.mvp.presenter;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.model.ModelNewsContent;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;

public class PresenterNewsContent {

    IFragmentNewsContent mainView;
    ModelNewsContent modelNewsContent;

    public PresenterNewsContent(IFragmentNewsContent fragmentNewsContent) {
        mainView = fragmentNewsContent;
        modelNewsContent = new ModelNewsContent(this);
    }

    public void init(String newsId) {
        modelNewsContent.getAllItems(newsId);
    }


    //TODO: callBack methods from Model to View

    public void initContent(NewsContentModel model) {
        mainView.initContent(model);
    }


    public void initLastNews(List<NewsSimpleModel> items) {
        mainView.initLastNews(items);
    }


    //########################################################################


}
