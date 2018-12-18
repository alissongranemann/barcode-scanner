package br.ufsc.barcodescanner;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Barcode.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BarcodeDao itemDao();
}
