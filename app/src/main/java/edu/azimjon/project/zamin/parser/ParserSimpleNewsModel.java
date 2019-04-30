package edu.azimjon.project.zamin.parser;

import android.arch.lifecycle.Observer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

//Simple news model parser
public class ParserSimpleNewsModel {
    //list of ids which are favourite
    List<NewsCategoryModel> categoryModels;

    public ParserSimpleNewsModel() {

    }

    public List<NewsSimpleModel> parse(JsonObject json) {
        categoryModels = NavigationActivity.getAllCategories();

        List<NewsSimpleModel> items = new ArrayList<>();

        JsonArray articles = json.getAsJsonArray("articles");

        for (JsonElement i : articles) {
            NewsSimpleModel model = new NewsSimpleModel();

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