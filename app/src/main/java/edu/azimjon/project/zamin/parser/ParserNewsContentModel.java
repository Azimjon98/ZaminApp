package edu.azimjon.project.zamin.parser;

import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.model.ContentNewsModel;
import edu.azimjon.project.zamin.model.CategoryNewsModel;

import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

//Content news model parser
public class ParserNewsContentModel {

    static public ContentNewsModel parse(JsonObject json) {
        ContentNewsModel model = new ContentNewsModel();
        List<CategoryNewsModel> categoryModels = NavigationActivity.categoryModels;

        try {
            //parsing and making a model
            model.setNewsId(json.getAsJsonPrimitive("newsID").getAsString());
            model.setTitle(json.getAsJsonPrimitive("title").getAsString());
            model.setDate(json.getAsJsonPrimitive("publishedAt").getAsString());
            model.setCategoryId(json.getAsJsonPrimitive("categoryID").getAsString());
            model.setOriginalUrl(json.getAsJsonPrimitive("url").getAsString());
            model.setImageUrl(json.getAsJsonPrimitive("urlToImage").getAsString());
            model.setViewedCount(json.getAsJsonPrimitive("viewed").getAsString());
            model.setContentUrl(json.getAsJsonPrimitive("urlparser").getAsString());

        } catch (Exception e) {
            Log.d(ERROR_LOG, "Error SimpleNews Parser: " + e.getMessage());
        }

        for (CategoryNewsModel c : categoryModels) {
            if (model.getCategoryId().equals(c.getCategoryId())) {

                model.setCategoryName(c.getName());
                break;
            }
        }

        return model;
    }
}