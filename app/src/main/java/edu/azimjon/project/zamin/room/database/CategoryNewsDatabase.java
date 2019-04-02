package edu.azimjon.project.zamin.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.dao.FavouriteNewsDao;


@Database(entities = NewsCategoryModel.class, version = 1, exportSchema = false)
public abstract class CategoryNewsDatabase extends RoomDatabase {
    private static CategoryNewsDatabase instance;

    public abstract CategoryNewsDao getDao();

    public static synchronized CategoryNewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CategoryNewsDatabase.class, "category_news")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
