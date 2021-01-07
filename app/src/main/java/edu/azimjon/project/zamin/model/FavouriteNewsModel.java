package edu.azimjon.project.zamin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourites_news")
public class FavouriteNewsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private
    int id;

    @ColumnInfo(name = "newsId")
    public String newsId;

    public String title;

    public String imageUrl;

    public String originalUrl;

    public String categoryId;

    public String categoryName;

    public String date;

    public boolean isWished = false;

    private String lastLocale;

    public FavouriteNewsModel() {
    }

    protected FavouriteNewsModel(Parcel in) {
        id = in.readInt();
        newsId = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        originalUrl = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        date = in.readString();
        isWished = in.readByte() != 0;
        lastLocale = in.readString();
    }

    public static final Creator<FavouriteNewsModel> CREATOR = new Creator<FavouriteNewsModel>() {
        @Override
        public FavouriteNewsModel createFromParcel(Parcel in) {
            return new FavouriteNewsModel(in);
        }

        @Override
        public FavouriteNewsModel[] newArray(int size) {
            return new FavouriteNewsModel[size];
        }
    };

    public void setLastLocale(String lastLocale) {
        this.lastLocale = lastLocale;
    }

    public String getLastLocale() {
        return lastLocale;
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
        dest.writeByte((byte) (isWished ? 1 : 0));
        dest.writeString(lastLocale);
    }
}
