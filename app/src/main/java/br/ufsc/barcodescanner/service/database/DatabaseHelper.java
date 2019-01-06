package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseHelper {

    private static AppDatabase INSTANCE;

    private DatabaseHelper() {

    }

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, AppDatabase.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }


}