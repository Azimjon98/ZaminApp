package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_category")
public class NewsCategoryModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    String name;

    int imageUrl;

    boolean isEnabled;

    public NewsCategoryModel(){}


    public NewsCategoryModel(String name, int imageUrl, boolean isEnabled) {
        this();
        this.name = name;
        this.imageUrl = imageUrl;
        this.isEnabled = isEnabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
