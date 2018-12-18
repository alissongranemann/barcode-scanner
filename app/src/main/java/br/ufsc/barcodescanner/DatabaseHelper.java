package br.ufsc.barcodescanner;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHelper {

    private static AppDatabase INSTANCE;

    private static String DATABASE_NAME = "barcode_db";

    private DatabaseHelper() {

    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .build();
        }

        return INSTANCE;
    }


}