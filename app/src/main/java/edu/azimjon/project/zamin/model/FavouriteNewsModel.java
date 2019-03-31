package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favourites_news")

public class FavouriteNewsModel {


    @PrimaryKey(autoGenerate = true)
    int id;

    String date;


}
