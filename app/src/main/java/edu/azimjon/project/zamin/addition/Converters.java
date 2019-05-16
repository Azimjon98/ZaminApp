package edu.azimjon.project.zamin.addition;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.ContentNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;

public class Converters {

    //TODO: To Favourite News

    public static FavouriteNewsModel fromSimpleNewstoFavouriteNews(SimpleNewsModel from) {
        FavouriteNewsModel model = new FavouriteNewsModel();

        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setWished(from.isWished());

        return model;
    }

    public static FavouriteNewsModel fromContentNewstoFavouritesNews(ContentNewsModel from) {
        FavouriteNewsModel model = new FavouriteNewsModel();

        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setWished(from.isWished());

        return model;
    }

//###############################################################################################

    //TODO: Back to Simple News

    public static SimpleNewsModel fromFavouritestoSimpleNews(FavouriteNewsModel from) {
        SimpleNewsModel model = new SimpleNewsModel();

        model.setCategoryId(from.getCategoryId());
        model.setCategoryName(from.getCategoryName());
        model.setDate(from.getDate());
        model.setImageUrl(from.getImageUrl());
        model.setNewsId(from.getNewsId());
        model.setOriginalUrl(from.getOriginalUrl());
        model.setTitle(from.getTitle());
        model.setWished(from.isWished());

        return model;
    }

    //##############################################################################################
}
