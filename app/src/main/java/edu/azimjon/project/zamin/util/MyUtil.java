package edu.azimjon.project.zamin.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Objects;

import edu.azimjon.project.zamin.addition.MySettings;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MyUtil {
    public static void showSoftKeyboard(final Context context, final EditText editText) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    public static void closeSoftKeyboard(Activity activity) {

        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }

    public static int pxToDp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (px / metrics.density);
    }

    public static int spToPx(int sp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static int pxToSp(int px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / (int) metrics.scaledDensity;
    }

    public static int getScreenWidth(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return outMetrics.heightPixels;
    }

    public static void setIconColor(ImageView iconHolder, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(iconHolder.getDrawable());
        DrawableCompat.setTint(wrappedDrawable, color);
        iconHolder.setImageDrawable(wrappedDrawable);
        iconHolder.invalidate();
    }

    /**
     * Gets a reference to a given drawable and prepares it for use with tinting through.
     *
     * @param resId the resource id for the given drawable
     * @return a wrapped drawable ready fo use
     * with {@link android.support.v4.graphics.drawable.DrawableCompat}'s tinting methods
     * @throws Resources.NotFoundException
     */
    public static Drawable getWrappedDrawable(Context context, @DrawableRes int resId) throws Resources.NotFoundException {
        return DrawableCompat.wrap(Objects.requireNonNull(ResourcesCompat.getDrawable(context.getResources(),
                resId, null)));
    }

    public static int getColor(Context context, @ColorRes int resId) throws Resources.NotFoundException {
        return ContextCompat.getColor(context, resId);
    }

    public static void removeGlobalLayoutObserver(View view, ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
    }

    public static Activity getHostActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    //check file to exists before using this method
    public static String readFileAsString(String filePath) {

        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            char current;
            StringBuilder builder = new StringBuilder();
            while (fis.available() > 0) {
                builder.append(String.valueOf((char) fis.read()));
            }

            return builder.toString();

        } catch (Exception e) {
            Log.d("TourGuide", e.toString());
        }


        return "";
    }

    public static boolean hasConnectionToNet(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        //there are a connection to net
        if (activeNetwork != null
                && activeNetwork.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLocalizedString(Context context, int resourceId) {
        Configuration configuration = context.getResources().getConfiguration();
        String language = MySettings.getInstance().getLocale();
        configuration.setLocale(new Locale(language));
        return context.createConfigurationContext(configuration).getResources().getString(resourceId);
    }

    public static String[] getLocalizedArray(Context context, int resourceId) {
        Configuration configuration = context.getResources().getConfiguration();
        String language = MySettings.getInstance().getLocale();
        configuration.setLocale(new Locale(language));
        return context.createConfigurationContext(configuration).getResources().getStringArray(resourceId);
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }



}
