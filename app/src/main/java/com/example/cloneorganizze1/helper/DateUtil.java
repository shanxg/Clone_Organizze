package com.example.cloneorganizze1.helper;

import java.text.SimpleDateFormat;

public class DateUtil {

    public static String actualDate(){

        long date = System.currentTimeMillis();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("d/M/yyyy");
        String dateString = mDateFormat.format(date);

        return dateString;
    }

    public static String monthID(String date){

        String choosenDate[] = date.split("/");
        String m = choosenDate[1];
        String y = choosenDate[2];

        return m+y;
    }


}
