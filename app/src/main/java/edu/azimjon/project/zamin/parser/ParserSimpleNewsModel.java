package edu.azimjon.project.zamin.parser;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;

import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

//Simple news model parser
public class ParserSimpleNewsModel {

    public static List<SimpleNewsModel> parse(JsonObject json, int type) {
        List<SimpleNewsModel> items = new ArrayList<>();
        List<CategoryNewsModel> categoryModels = NavigationActivity.categoryModels;


        for (JsonElement i : json.getAsJsonArray("articles")) {
            SimpleNewsModel model = new SimpleNewsModel();

            JsonObject article = i.getAsJsonObject();

            try {
                //parsing and making a model
                model.setNewsId(article.getAsJsonPrimitive("newsID").getAsString());
                model.setTitle(article.getAsJsonPrimitive("title").getAsString());
                model.setDate(article.getAsJsonPrimitive("publishedAt").getAsString());
                model.setCategoryId(article.getAsJsonPrimitive("categoryID").getAsString());
                model.setOriginalUrl(article.getAsJsonPrimitive("url").getAsString());
                model.setImageUrl(article.getAsJsonPrimitive("urlToImage").getAsString());

            } catch (Exception e) {
                Log.d(ERROR_LOG, "Error SimpleNews Parser: " + e.getMessage());
            }


            if (type == 3) {
                model.setUrlAudioFile(article.getAsJsonPrimitive("urlToAudio").getAsString());
            } else if (type == 2) {
                //some code for video
            } else if (type == 1) {
                model.titleImages = new String[3];
                model.titleImages[0] = model.getImageUrl();
                model.titleImages[1] = article.getAsJsonPrimitive("urlToImage2").getAsString();
                model.titleImages[2] = article.getAsJsonPrimitive("urlToImage3").getAsString();
            }

            for (CategoryNewsModel c : categoryModels) {
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