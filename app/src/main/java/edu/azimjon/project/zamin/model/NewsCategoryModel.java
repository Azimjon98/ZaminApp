package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_category")
public class NewsCategoryModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    String name;

    String categoryId;

    String imageUrl;

    boolean isEnabled;

    public NewsCategoryModel() {
        isEnabled = true;
    }

    @Ignore
    public NewsCategoryModel(String name, String categoryId, String imageUrl) {
        this();
        this.name = name;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
