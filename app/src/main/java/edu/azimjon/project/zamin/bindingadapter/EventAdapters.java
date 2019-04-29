package edu.azimjon.project.zamin.bindingadapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.component.MyApplicationComponent;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

public class EventAdapters {

    @BindingAdapter("observeIsWished")
    public static void observeIsWished(ImageView view, String messege) {
        if (messege.equals("false"))
            return;

        Context context = null;
        try {
            context = MyApplication.getInstance();

        } catch (ClassNotFoundException e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }


        FavouriteNewsDatabase.getInstance(context).getDao().getAllIdsLive().observe(new Fragment(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {

            }
        });
    }

}
