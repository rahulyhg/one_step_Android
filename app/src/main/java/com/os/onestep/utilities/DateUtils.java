package com.os.onestep.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MeetBrahmbhatt1 on 30/06/16.
 */
public class DateUtils {

    public static int getDayFromDateString(String str_date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(str_date);
        String day = (String) android.text.format.DateFormat.format("dd", date);
        return Integer.valueOf(day);
    }

    public static int getMonthFromDateString(String str_date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(str_date);
        String month = (String) android.text.format.DateFormat.format("MM", date);
        return Integer.valueOf(month);
    }

    public static int getYearFromDateString(String str_date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = format.parse(str_date);
        String year = (String) android.text.format.DateFormat.format("yyyy", date);
        return Integer.valueOf(year);
    }
}
