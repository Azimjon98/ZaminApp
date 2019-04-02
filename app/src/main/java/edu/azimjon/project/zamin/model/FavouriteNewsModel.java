package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourites_news")
public class FavouriteNewsModel extends BaseNewsModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    String savedDate;

    public FavouriteNewsModel() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;

    }

    public int getId() {
        return id;
    }

    public String getSavedDate() {
        return savedDate;
    }
}
