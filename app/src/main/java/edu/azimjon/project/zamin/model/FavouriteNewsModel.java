package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourites_news")
public class FavouriteNewsModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "newsId")
    public String newsId;

    public String title;

    public String imageUrl;

    public String originalUrl;

    public String categoryId;

    public String categoryName;

    public String date;

    public String viewedCount;

    public boolean isWished;

    String savedDate;

    public FavouriteNewsModel() {
        isWished = false;
    }

    public FavouriteNewsModel(String newsId, String title, String imageUrl, String originalUrl, String categoryId, String date, String viewedCount, boolean isWished, String savedDate) {
        this.newsId = newsId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.originalUrl = originalUrl;
        this.categoryId = categoryId;
        this.date = date;
        this.viewedCount = viewedCount;
        this.isWished = isWished;
        this.savedDate = savedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setViewedCount(String viewedCount) {
        this.viewedCount = viewedCount;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

    public int getId() {
        return id;
    }

    public String getNewsId() {
        return newsId;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDate() {
        return date;
    }

    public String getViewedCount() {
        return viewedCount;
    }

    public boolean isWished() {
        return isWished;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
