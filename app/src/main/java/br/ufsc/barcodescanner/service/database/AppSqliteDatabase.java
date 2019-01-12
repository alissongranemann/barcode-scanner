package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.ufsc.barcodescanner.service.database.dao.BarcodeDao;
import br.ufsc.barcodescanner.service.model.Barcode;

@Database(entities = {Barcode.class}, version = 1)
public abstract class AppSqliteDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "barcode_db";

    public abstract BarcodeDao itemDao();
}
