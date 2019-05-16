package edu.azimjon.project.zamin.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import edu.azimjon.project.zamin.model.CategoryNewsModel;

@Dao
public abstract class CategoryNewsDao {

    @Insert
    abstract public void insert(CategoryNewsModel entity);

    @Insert
    abstract public void insertAll(List<CategoryNewsModel> entites);

    @Update
    abstract public void update(CategoryNewsModel entity);

    @Query("DELETE FROM news_category WHERE id = :id")
    abstract public void delete(String id);

    @Query("DELETE FROM news_category")
    abstract public void deleteAll();

    @Query("SELECT * FROM news_category")
    abstract public LiveData<List<CategoryNewsModel>> getAllLive();

    @Query("SELECT * FROM news_category")
    abstract public List<CategoryNewsModel> getAll();

    @Query("SELECT * FROM news_category WHERE isEnabled = 1")
    abstract public LiveData<List<CategoryNewsModel>> getAllEnabledCategoriesLive();

    @Query("SELECT * FROM news_category WHERE isEnabled = 1")
    abstract public List<CategoryNewsModel> getAllEnabledCategories();

    @Transaction
    public synchronized void deleteAndCreate(List<CategoryNewsModel> users) {
        deleteAll();
        insertAll(users);
    }
}
