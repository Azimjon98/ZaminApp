package edu.azimjon.project.zamin.bindingadapter;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.component.MyApplicationComponent;


public class ImageBindingAdapters {


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, int id) {


        try {
            MyApplication.getInstance()
                    .getMyApplicationComponent()
                    .getGlideManager()
                    .load(id)
                    .into(view);

        } catch (ClassNotFoundException e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url == null)
            return;

        try {
            Glide.with(view)
                    .load(url)
                    .into(view);

        } catch (Exception e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @BindingAdapter("imageUrlSmallQuality")
    public static void loadImageSmallQuality(ImageView view, String url) {
        if (url == null)
            return;


        try {
            MyApplication.getInstance()
                    .getMyApplicationComponent()
                    .getGlideManager()
                    .load(url + "&width=300&quality=40")
                    .into(view);

        } catch (ClassNotFoundException e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @BindingAdapter("imageUrlMediumQuality")
    public static void loadImageMediumQuality(ImageView view, String url) {
        if (url == null)
            return;


        try {
            MyApplication.getInstance()
                    .getMyApplicationComponent()
                    .getGlideManager()
                    .load(url + "&width=300&quality=70")
                    .into(view);

        } catch (ClassNotFoundException e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(Constants.ERROR_LOG, "error in loadimage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @BindingAdapter("giveCornerRadius")
    public static void giveCornerRadius(ImageView view, String url) {

        //make image view with corners
        view.setBackgroundResource(R.drawable.make_imageview_with_corners);
        view.setClipToOutline(true);


    }

}