package edu.azimjon.project.zamin.bindingadapter;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.component.MyApplicationComponent;
import edu.azimjon.project.zamin.model.NewsCategoryModel;

public class TextBindingAdapters {

    @BindingAdapter("mySetDate")
    public static void setDate(TextView view, String text) {
        if (text == null)
            return;

        //making date of today 00:00 time
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date todayDate = calendar.getTime();
        Date newsDate = null;
        try {
            newsDate = format.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();

            //set input text and return
            view.setText(text);
            return;
        }

        String textToday = view.getContext().getApplicationContext().getResources().getString(R.string.today);
        String[] months = view.getContext().getResources().getStringArray(R.array.months);
        Calendar newsCalendar = new GregorianCalendar();
        calendar.setTimeInMillis(newsDate.getTime());

        if (newsDate.after(todayDate)) {
            view.setText(
                    new SimpleDateFormat(" HH:mm").format(newsDate) +
                            " • " + textToday
            );
        } else {
            view.setText(
                    new SimpleDateFormat("HH:mm • d ").format(newsDate) +
                            months[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR)
            );
        }

    }

    @BindingAdapter("setCategoryName")
    public static void setCategoryName(TextView view, String text) {
//        MyApplicationComponent component = null;
//        List<NewsCategoryModel> categoryModels;
////        while (NavigationActivity.getAllCategories() == null || MySettings.getInstance().islangChanged()) ;
//        categoryModels = NavigationActivity.getAllCategories();
//
//        for (NewsCategoryModel c : categoryModels) {
//            if (text.equals(c.getCategoryId())) {
//
//                view.setText(c.getName());
//                break;
//            }
//        }
    }
}