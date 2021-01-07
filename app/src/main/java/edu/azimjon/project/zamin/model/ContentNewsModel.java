package edu.azimjon.project.zamin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_content")
public class ContentNewsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private
    int id;

    @ColumnInfo(name = "newsId")
    private String newsId;

    private String title;

    private String imageUrl;

    private String originalUrl;

    private String categoryId;

    private String categoryName;

    private String date;

    private String viewedCount;

    public boolean isWished;

    private String contentUrl;


    public ContentNewsModel() {
        isWished = false;
    }

    protected ContentNewsModel(Parcel in) {
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
        contentUrl = in.readString();
    }

    public static final Creator<ContentNewsModel> CREATOR = new Creator<ContentNewsModel>() {
        @Override
        public ContentNewsModel createFromParcel(Parcel in) {
            return new ContentNewsModel(in);
        }

        @Override
        public ContentNewsModel[] newArray(int size) {
            return new ContentNewsModel[size];
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

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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

    public String getContentUrl() {
        return contentUrl;
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
        dest.writeString(contentUrl);
    }
}
