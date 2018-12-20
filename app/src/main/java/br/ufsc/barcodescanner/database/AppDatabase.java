package br.ufsc.barcodescanner.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.ufsc.barcodescanner.entity.Barcode;
import br.ufsc.barcodescanner.database.dao.BarcodeDao;

@Database(entities = {Barcode.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "barcode_db";

    public abstract BarcodeDao itemDao();
}
