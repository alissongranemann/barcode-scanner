package br.ufsc.barcodescanner.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

    public static String timestampToString(long value) {
        return df.format(new Date(value));
    }


}