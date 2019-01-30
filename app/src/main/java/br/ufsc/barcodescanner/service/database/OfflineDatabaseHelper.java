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

public class OfflineDatabaseHelper {

    private static OfflineDatabase INSTANCE;
    private static String TAG = "OfflineDatabaseHelper";

    private OfflineDatabaseHelper() {
    }

    public static OfflineDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    OfflineDatabase.class, OfflineDatabase.DATABASE_NAME)
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
                    //.allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

}