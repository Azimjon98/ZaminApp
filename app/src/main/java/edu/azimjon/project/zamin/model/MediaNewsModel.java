package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "media_simple")
public class MediaNewsModel implements Parcelable {

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

    String urlAudioFile;

    public String[] titleImages;

    public MediaNewsModel() {
        isWished = false;
        titleImages = new String[3];
    }

    protected MediaNewsModel(Parcel in) {
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
        urlAudioFile = in.readString();
        titleImages = in.createStringArray();
    }

    public static final Creator<MediaNewsModel> CREATOR = new Creator<MediaNewsModel>() {
        @Override
        public MediaNewsModel createFromParcel(Parcel in) {
            return new MediaNewsModel(in);
        }

        @Override
        public MediaNewsModel[] newArray(int size) {
            return new MediaNewsModel[size];
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

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public void setTitleImages(String[] titleImages) {
        this.titleImages = titleImages;
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

    public String getCategoryName() {
        return categoryName;
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

    public String[] getTitleImages() {
        return titleImages;
    }

    public void setUrlAudioFile(String urlAudioFile) {
        this.urlAudioFile = urlAudioFile;
    }

    public String getUrlAudioFile() {
        return urlAudioFile;
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
        dest.writeString(urlAudioFile);
        dest.writeStringArray(titleImages);
    }
}
