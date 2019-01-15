package br.ufsc.barcodescanner.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    static DateFormat df = new SimpleDateFormat("HH:mm - dd/MM/yyyy");

    public static String dateToString(Date value) {
        return value == null ? null : df.format(value);
    }


}