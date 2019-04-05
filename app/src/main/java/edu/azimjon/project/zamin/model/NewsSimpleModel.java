package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_simple")
public class NewsSimpleModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "newsId")
    String newsId;

    String title;

    String imageUrl;

    String originalUrl;

    String categoryId;
    String categoryName;

    String date;

    String viewedCount;

    boolean isWished;


    public NewsSimpleModel() {
        isWished = false;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}