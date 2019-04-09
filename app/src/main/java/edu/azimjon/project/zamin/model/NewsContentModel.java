package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "news_content")
public class NewsContentModel implements Parcelable {

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

    String content;


    public NewsContentModel() {
        isWished = false;
    }

    protected NewsContentModel(Parcel in) {
        id = in.readInt();
        newsId = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        originalUrl = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        date = in.readString();
        viewedCount = in.readString();
        isWished = in.readByte() != 0;
        content = in.readString();
    }

    public static final Creator<NewsContentModel> CREATOR = new Creator<NewsContentModel>() {
        @Override
        public NewsContentModel createFromParcel(Parcel in) {
            return new NewsContentModel(in);
        }

        @Override
        public NewsContentModel[] newArray(int size) {
            return new NewsContentModel[size];
        }
    };

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

    public void setContent(String content) {
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(newsId);
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(originalUrl);
        dest.writeString(categoryId);
        dest.writeString(categoryName);
        dest.writeString(date);
        dest.writeString(viewedCount);
        dest.writeByte((byte) (isWished ? 1 : 0));
        dest.writeString(content);
    }
}
