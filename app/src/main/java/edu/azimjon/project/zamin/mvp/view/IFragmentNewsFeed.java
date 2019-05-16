package edu.azimjon.project.zamin.mvp.view;

import java.util.List;

import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;

public interface IFragmentNewsFeed {

    void initCategories(List<CategoryNewsModel> items, int messege);

    void initMainNews(List<SimpleNewsModel> items, int messege);

    void initLastAndContinueNews(List<SimpleNewsModel> items, int messege);

    void initAudioNews(List<SimpleNewsModel> items, int messege);

    void initVideoNews(List<SimpleNewsModel> items, int messege);

    void addLastNewsContinue(List<SimpleNewsModel> items, int messege);
}
