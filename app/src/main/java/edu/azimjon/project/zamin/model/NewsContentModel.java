package edu.azimjon.project.zamin.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news_content")
public class NewsContentModel extends BaseNewsModel {

    @PrimaryKey(autoGenerate = true)
    int id;

    String content;


    public NewsContentModel() {
        super();
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
