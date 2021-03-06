package edu.azimjon.project.zamin.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;


@Database(entities = CategoryNewsModel.class, version = 1)
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
