package edu.azimjon.project.zamin.parser;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.fragment.FragmentWebView;
import edu.azimjon.project.zamin.model.MediaNewsModel;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

//Simple news model parser
public class ParserMediaNewsModel {
    //list of ids which are favourite
    List<NewsCategoryModel> categoryModels;

    public ParserMediaNewsModel() {

    }

    public List<MediaNewsModel> parse(JsonObject json, int type) {
        categoryModels = NavigationActivity.getAllCategories();

        List<MediaNewsModel> items = new ArrayList<>();

        JsonArray articles = json.getAsJsonArray("articles");

        for (JsonElement i : articles) {
            MediaNewsModel model = new MediaNewsModel();

            JsonObject article = i.getAsJsonObject();

            try {
                //parsing and making a model
                model.setNewsId(article.getAsJsonPrimitive("newsID").getAsString());
                model.setTitle(article.getAsJsonPrimitive("title").getAsString());
                model.setDate(article.getAsJsonPrimitive("publishedAt").getAsString());
                model.setCategoryId(article.getAsJsonPrimitive("categoryID").getAsString());
                model.setOriginalUrl(article.getAsJsonPrimitive("url").getAsString());
                model.setViewedCount(article.getAsJsonPrimitive("viewed").getAsString());

                if (type == 3) {

                } else if (type == 2) {
                    model.setImageUrl(article.getAsJsonPrimitive("urlToImage").getAsString());
                } else if (type == 1) {
                    model.titleImages[0] = article.getAsJsonPrimitive("urlToImage").getAsString();
                    model.titleImages[1] = article.getAsJsonPrimitive("urlToImage2").getAsString();
                    model.titleImages[2] = article.getAsJsonPrimitive("urlToImage3").getAsString();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(ERROR_LOG, "Error MediaNews Parser: " + e.getMessage());
            }


            for (NewsCategoryModel c : categoryModels) {
                if (model.getCategoryId().equals(c.getCategoryId())) {

                    model.setCategoryName(c.getName());
                    break;
                }
            }

            items.add(model);

        }

        return items;
    }

}