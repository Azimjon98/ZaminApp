package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_simple")
public class NewsSimpleModel extends BaseNewsModel {

    @PrimaryKey(autoGenerate = true)
    int id;


    public NewsSimpleModel() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}