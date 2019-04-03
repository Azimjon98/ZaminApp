package edu.azimjon.project.zamin.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

//Simple news model parser
public class ParserNewsContentModel {

    public static NewsContentModel parse(JsonObject json) {


        JsonObject article = json;
        NewsContentModel model = new NewsContentModel();

        //parsing and making a model
        model.setNewsId(article.getAsJsonPrimitive("newsID").getAsString());
        model.setTitle(article.getAsJsonPrimitive("title").getAsString());
        model.setDate(article.getAsJsonPrimitive("publishedAt").getAsString());
        model.setCategoryId(article.getAsJsonPrimitive("categoryID").getAsString());
        model.setOriginalUrl(article.getAsJsonPrimitive("url").getAsString());
        model.setImageUrl(article.getAsJsonPrimitive("urlToImage").getAsString());
        model.setViewedCount(article.getAsJsonPrimitive("viewed").getAsString());
        model.setContent(article.getAsJsonPrimitive("content").getAsString());

        return model;
    }
}