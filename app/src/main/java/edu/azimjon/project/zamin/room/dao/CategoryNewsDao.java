package edu.azimjon.project.zamin.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsCategoryModel;

@Dao
public abstract class CategoryNewsDao {

    @Insert
    abstract public void insert(NewsCategoryModel entity);

    @Insert
    abstract public void insertAll(List<NewsCategoryModel> entites);

    @Update
    abstract public void update(NewsCategoryModel entity);

    @Query("DELETE FROM news_category WHERE id = :id")
    abstract public void delete(String id);

    @Query("DELETE FROM news_category")
    abstract public void deleteAll();

    @Query("SELECT * FROM news_category")
    abstract public LiveData<List<NewsCategoryModel>> getAllLive();

    @Query("SELECT * FROM news_category")
    abstract public List<NewsCategoryModel> getAll();

    @Query("SELECT * FROM news_category WHERE isEnabled = 1")
    abstract public LiveData<List<NewsCategoryModel>> getAllEnabledCategoriesLive();

    @Query("SELECT * FROM news_category WHERE isEnabled = 1")
    abstract public List<NewsCategoryModel> getAllEnabledCategories();

    @Transaction
    public void deleteAndCreate(List<NewsCategoryModel> users) {
        deleteAll();
        insertAll(users);
    }
}
