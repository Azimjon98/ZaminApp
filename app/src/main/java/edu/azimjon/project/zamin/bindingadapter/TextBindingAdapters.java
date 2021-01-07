package edu.azimjon.project.zamin.bindingadapter;

import android.content.Context;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.util.MyUtil;

public class TextBindingAdapters {

    @BindingAdapter("mySetDate")
    public static void setDate(TextView view, String text) {
        Context context = null;
        try {
            context = MyApplication.getInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

        String textToday = MyUtil.getLocalizedString(context,R.string.today);
        String[] months = MyUtil.getLocalizedArray(context,R.array.months);
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

}