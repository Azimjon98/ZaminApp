package edu.azimjon.project.zamin.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;

@Dao
public interface FavouriteNewsDao {

    @Insert
    void insert(FavouriteNewsModel entity);

    @Update
    void update(FavouriteNewsModel entity);

    @Query("UPDATE favourites_news SET title = :title WHERE newsId = :newsId")
    void updateTitle(String newsId, String title);

    @Query("DELETE FROM favourites_news WHERE newsId = :id")
    void delete(String id);

    @Query("DELETE FROM favourites_news")
    void deleteAll();

    @Query("SELECT * FROM favourites_news")
    LiveData<List<FavouriteNewsModel>> getAll();

    @Query("SELECT favourites_news.newsId FROM favourites_news")
    LiveData<List<String>> getAllIdsLive();

    @Query("SELECT favourites_news.newsId FROM favourites_news")
    List<String> getAllIds();

}
