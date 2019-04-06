package edu.azimjon.project.zamin.parser;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

//Simple news model parser
public class ParserSimpleNewsModel {
    //list of ids which are favourite
    List<String> allFavouriteIds;
    List<NewsCategoryModel> categoryModels;

    public ParserSimpleNewsModel(Fragment fragment) {
        allFavouriteIds = NavigationActivity.getFavouritesIds();
        categoryModels = NavigationActivity.getAllCategories();
    }

    public List<NewsSimpleModel> parse(JsonObject json) {

        while (allFavouriteIds == null || categoryModels == null) {
            allFavouriteIds = NavigationActivity.getFavouritesIds();
            categoryModels = NavigationActivity.getAllCategories();
        }

        List<NewsSimpleModel> items = new ArrayList<>();

        JsonArray articles = json.getAsJsonArray("articles");

        for (JsonElement i : articles) {
            NewsSimpleModel model = new NewsSimpleModel();

            JsonObject article = i.getAsJsonObject();

            //parsing and making a model
            model.setNewsId(article.getAsJsonPrimitive("newsID").getAsString());
            model.setTitle(article.getAsJsonPrimitive("title").getAsString());
            model.setDate(article.getAsJsonPrimitive("publishedAt").getAsString());
            model.setCategoryId(article.getAsJsonPrimitive("categoryID").getAsString());
            model.setOriginalUrl(article.getAsJsonPrimitive("url").getAsString());
            model.setImageUrl(article.getAsJsonPrimitive("urlToImage").getAsString());
            model.setViewedCount(article.getAsJsonPrimitive("viewed").getAsString());

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

            items.add(model);

        }

        return items;
    }
}