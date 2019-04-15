package edu.azimjon.project.zamin.addition;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

public class Converters {

    public static FavouriteNewsModel fromSimpleNewstoFavouriteNews(NewsSimpleModel from) {
        FavouriteNewsModel model = new FavouriteNewsModel();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD hh-mm");

        model.setSavedDate(format.format(new Date()));
        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setViewedCount(from.getViewedCount());
        model.setWished(from.isWished());

        return model;
    }

    public static NewsContentModel fromSimpleNewstoContentNews(NewsSimpleModel from) {
        NewsContentModel model = new NewsContentModel();

        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setViewedCount(from.getViewedCount());
        model.setWished(from.isWished());

        return model;
    }

    public static FavouriteNewsModel fromContentNewstoFavouritesNews(NewsContentModel from) {
        FavouriteNewsModel model = new FavouriteNewsModel();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD hh-mm");

        model.setSavedDate(format.format(new Date()));
        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setViewedCount(from.getViewedCount());
        model.setWished(from.isWished());

        return model;
    }

    public static NewsSimpleModel fromFavouritestoSimpleNews(FavouriteNewsModel from) {
        NewsSimpleModel model = new NewsSimpleModel();


        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setViewedCount(from.getViewedCount());
        model.setWished(from.isWished());

        return model;
    }
}
