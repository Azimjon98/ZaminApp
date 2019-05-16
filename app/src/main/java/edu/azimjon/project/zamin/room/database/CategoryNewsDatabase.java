package edu.azimjon.project.zamin.room.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
