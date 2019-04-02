package edu.azimjon.project.zamin.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsCategoryModel;

@Dao
public interface CategoryNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsCategoryModel entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewsCategoryModel> entites);

    @Update
    void update(NewsCategoryModel entity);

    @Query("DELETE FROM news_category WHERE id = :id")
    void delete(String id);

    @Query("DELETE FROM news_category")
    void deleteAll();

    @Query("SELECT * FROM news_category")
    List<NewsCategoryModel> getAll();

    @Query("SELECT * FROM news_category WHERE isEnabled = 1")
    List<NewsCategoryModel> getAllEnabledCategories();
}
