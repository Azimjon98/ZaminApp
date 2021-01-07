package edu.azimjon.project.zamin.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.room.dao.FavouriteNewsDao;


@Database(entities = FavouriteNewsModel.class, version = 2)
public abstract class FavouriteNewsDatabase extends RoomDatabase {
    private static FavouriteNewsDatabase instance;

    public abstract FavouriteNewsDao getDao();

    public static synchronized FavouriteNewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FavouriteNewsDatabase.class, "favourite_news")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
