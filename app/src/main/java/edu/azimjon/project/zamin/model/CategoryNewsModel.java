package edu.azimjon.project.zamin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_category")
public class CategoryNewsModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private
    int id;

    private String name;

    private String categoryId;

    private String imageUrl;

    private boolean isEnabled;

    public CategoryNewsModel() {
        isEnabled = true;
    }

    @Ignore
    public CategoryNewsModel(String name, String categoryId, String imageUrl) {
        this();
        this.name = name;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
    }

    protected CategoryNewsModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        categoryId = in.readString();
        imageUrl = in.readString();
        isEnabled = in.readByte() != 0;
    }

    public static final Creator<CategoryNewsModel> CREATOR = new Creator<CategoryNewsModel>() {
        @Override
        public CategoryNewsModel createFromParcel(Parcel in) {
            return new CategoryNewsModel(in);
        }

        @Override
        public CategoryNewsModel[] newArray(int size) {
            return new CategoryNewsModel[size];
        }
    };



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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(categoryId);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
    }
}
