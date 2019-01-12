package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class SqliteDatabase {

    private static AppSqliteDatabase INSTANCE;

    private SqliteDatabase() {

    }

    public static AppSqliteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppSqliteDatabase.class, AppSqliteDatabase.DATABASE_NAME)
                    .allowMainThreadQueries() //TODO: remove
                    .build();
        }

        return INSTANCE;
    }


}