package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalDatabaseHelper {

    private static LocalDatabase INSTANCE;
    private static String TAG = "LocalDatabaseHelper";

    private LocalDatabaseHelper() {
    }

    public static LocalDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    LocalDatabase.class, LocalDatabase.DATABASE_NAME)
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            InputStream is = null;
                            try {
                                is = context.getAssets().open("db_init.sql");
                                String line;
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                while ((line = reader.readLine()) != null) {
                                    Log.i("SQL Script", line);
                                    if (!line.isEmpty() && !line.trim().startsWith("--"))
                                        db.execSQL(line);
                                }
                            } catch (IOException e) {
                                Log.e(TAG, "Error loading init SQL", e);
                            } finally {
                                IOUtils.closeQuietly(is);
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    //.allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

}