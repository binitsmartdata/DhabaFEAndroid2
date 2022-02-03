package com.transport.mall.ui.home.helpline.chat;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.transport.mall.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class Utils {
    private static long millis;

    public static long dateToLongCovvert(String mydate) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            date = sdf.parse(mydate);
            if (date != null) {
                millis = date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millis;
    }


    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "hh:mm aa";
        final String dateTimeFormatString = "yyyy-MM-dd, hh:mm aa";


        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
//            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else {
            return DateFormat.format("dd/MM/yyyy", smsTime).toString();
        }
    }

    public static String getFormatedDateTime(String strDate, String sourceFormate,
                                             String destinyFormate) {
        SimpleDateFormat df;
        df = new SimpleDateFormat(sourceFormate, Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        df = new SimpleDateFormat(destinyFormate, Locale.US);
        return df.format(date);
    }


    public static boolean compareTwoDate(String fisrtDate, String secondDate) {

        boolean sameDay = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date1 = formatter.parse(fisrtDate);
            Date date2 = formatter.parse(secondDate);


            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sameDay;
    }


    public static String getFormattedDateTime(long smsTimeInMilis, Context mContext) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EE, MM d, h:mm aa";
        final String dateFormatString = "dd/MM/yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            // return "Today" + DateFormat.format(timeFormatString, smsTime);
            return mContext.getString(R.string.chat_today);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            // return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
            return mContext.getString(R.string.chat_yesterday);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateFormatString, smsTime).toString();
        } else {
            return "" + DateFormat.format(dateFormatString, smsTime).toString();
        }
    }


    public static int getWidthScreenResolution(Context context) {
        int width = 0;
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
            int height = metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;

    }

    public static int getHeightScreenResolution(Context context) {
        int height = 0;
        try {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            height = metrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getWindow().getCurrentFocus();
            if (view != null && view.getWindowToken() != null) {
                IBinder binder = view.getWindowToken();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder, 0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

//            assert pasTime != null;
            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " second " + suffix;
            } else if (minute < 60) {
                convTime = minute + " minute " + suffix;
            } else if (hour < 24) {
                convTime = hour + " hour " + suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " year " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " month " + suffix;
                } else {
                    convTime = (day / 7) + " week " + suffix;
                }
            } else if (day < 7) {
                convTime = day + " day " + suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }

}
