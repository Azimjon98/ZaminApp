package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "news_simple")
public class SimpleNewsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private
    int id;

    @ColumnInfo(name = "newsId")
    private
    String newsId;

    private String title;

    private String imageUrl;

    private String originalUrl;

    private String categoryId;

    private String categoryName;

    private String date;

    private boolean isWished = false;

    private String urlAudioFile = null;

    public String[] titleImages = null;


    public SimpleNewsModel() {

    }


    protected SimpleNewsModel(Parcel in) {
        id = in.readInt();
        newsId = in.readString();
        title = in.readString();
        imageUrl = in.readString();
        originalUrl = in.readString();
        categoryId = in.readString();
        categoryName = in.readString();
        date = in.readString();
        isWished = in.readByte() != 0;
        urlAudioFile = in.readString();
        titleImages = in.createStringArray();
    }

    public static final Creator<SimpleNewsModel> CREATOR = new Creator<SimpleNewsModel>() {
        @Override
        public SimpleNewsModel createFromParcel(Parcel in) {
            return new SimpleNewsModel(in);
        }

        @Override
        public SimpleNewsModel[] newArray(int size) {
            return new SimpleNewsModel[size];
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

    public void setUrlAudioFile(String urlAudioFile) {
        this.urlAudioFile = urlAudioFile;
    }

    public void setTitleImages(String[] titleImages) {
        this.titleImages = titleImages;
    }

    public String getUrlAudioFile() {
        return urlAudioFile;
    }

    public String[] getTitleImages() {
        return titleImages;
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
        dest.writeString(urlAudioFile);
        dest.writeStringArray(titleImages);
    }
}