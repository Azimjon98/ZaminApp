package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "news_category")
public class NewsCategoryModel implements Parcelable {

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

    protected NewsCategoryModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        categoryId = in.readString();
        imageUrl = in.readString();
        isEnabled = in.readByte() != 0;
    }

    public static final Creator<NewsCategoryModel> CREATOR = new Creator<NewsCategoryModel>() {
        @Override
        public NewsCategoryModel createFromParcel(Parcel in) {
            return new NewsCategoryModel(in);
        }

        @Override
        public NewsCategoryModel[] newArray(int size) {
            return new NewsCategoryModel[size];
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
