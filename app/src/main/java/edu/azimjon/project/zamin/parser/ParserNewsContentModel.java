package edu.azimjon.project.zamin.parser;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

//Simple news model parser
public class ParserNewsContentModel {
    //list of ids which are favourite
    List<String> allFavouriteIds;
    List<NewsCategoryModel> categoryModels;

    public ParserNewsContentModel() {
        allFavouriteIds = NavigationActivity.getFavouritesIds();
        categoryModels = NavigationActivity.getAllCategories();
    }


    public NewsContentModel parse(JsonObject json) {


        JsonObject article = json;
        NewsContentModel model = new NewsContentModel();

        try {
            //parsing and making a model
            model.setNewsId(article.getAsJsonPrimitive("newsID").getAsString());
            model.setTitle(article.getAsJsonPrimitive("title").getAsString());
            model.setDate(article.getAsJsonPrimitive("publishedAt").getAsString());
            model.setCategoryId(article.getAsJsonPrimitive("categoryID").getAsString());
            model.setOriginalUrl(article.getAsJsonPrimitive("url").getAsString());
            model.setImageUrl(article.getAsJsonPrimitive("urlToImage").getAsString());
            model.setViewedCount(article.getAsJsonPrimitive("viewed").getAsString());
            model.setContent(article.getAsJsonPrimitive("urlparser").getAsString());

        } catch (Exception e) {
            Log.d(ERROR_LOG, "Error SimpleNews Parser: " + e.getMessage());
        }

        if (allFavouriteIds.contains(model.getNewsId())) {
            model.setWished(true);
        }

        for (NewsCategoryModel c : categoryModels) {
            if (model.getCategoryId().equals(c.getCategoryId())) {
                Log.d("myLog", "categoryModels in");

                model.setCategoryName(c.getName());
                break;
            }
        }

        return model;
    }
}