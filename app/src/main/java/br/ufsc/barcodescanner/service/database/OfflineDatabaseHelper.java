package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.model.Subgroup;

public class OfflineDatabaseHelper {

    private static OfflineDatabase INSTANCE;

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
                            Executors.newSingleThreadScheduledExecutor().execute(() -> { //TODO: fix race condition
                                getInstance(context).groupDao().insertAll(Group.populateData());
                                getInstance(context).subgroupDao().insertAll(Subgroup.populateData());
                            });
                        }
                    })
                    //.allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }


}