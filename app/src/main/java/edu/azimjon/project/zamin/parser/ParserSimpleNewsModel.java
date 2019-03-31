package edu.azimjon.project.zamin.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.model.NewsSimpleModel;

//Simple news model parser
public class ParserSimpleNewsModel {

    public static List<NewsSimpleModel> parse(JsonObject json) {

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


            items.add(model);

        }

        return items;
    }
}
