package br.ufsc.barcodescanner.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import br.ufsc.barcodescanner.database.AppDatabase;

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