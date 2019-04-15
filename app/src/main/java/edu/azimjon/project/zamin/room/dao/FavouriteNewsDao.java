package edu.azimjon.project.zamin.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;

@Dao
public interface FavouriteNewsDao {

    @Insert
    void insert(FavouriteNewsModel entity);

    @Update
    void update(FavouriteNewsModel entity);

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
