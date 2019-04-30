package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "news_simple")
public class NewsSimpleModel implements Parcelable {

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

    boolean isWished;


    public NewsSimpleModel() {
        isWished = false;
    }

    protected NewsSimpleModel(Parcel in) {
        id = in.readInt();
        newsId = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        originalUrl = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        date = in.readString();
        isWished = in.readByte() != 0;
    }

    public static final Creator<NewsSimpleModel> CREATOR = new Creator<NewsSimpleModel>() {
        @Override
        public NewsSimpleModel createFromParcel(Parcel in) {
            return new NewsSimpleModel(in);
        }

        @Override
        public NewsSimpleModel[] newArray(int size) {
            return new NewsSimpleModel[size];
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

    public boolean isWished() {
        return isWished;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
        dest.writeByte((byte) (isWished ? 1 : 0));
    }
}