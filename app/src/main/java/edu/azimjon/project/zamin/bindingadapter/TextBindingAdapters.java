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

import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.component.MyApplicationComponent;

public class TextBindingAdapters {

    @BindingAdapter("mySetDate")
    public static void setDate(TextView view, String text) {
        MyApplicationComponent component = null;

        //making date of today 00:00 time
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");


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


        if (newsDate.after(todayDate)) {
            view.setText("bugun " +
                    new SimpleDateFormat(" HH:mm").format(newsDate)
            );
        } else {
            view.setText(
                    new SimpleDateFormat(" dd MM yyyy  HH:mm").format(newsDate)
            );
        }

    }

    @BindingAdapter("setCategoryName")
    public static void setCategoryName(TextView view, String text) {
        MyApplicationComponent component = null;


    }


}
