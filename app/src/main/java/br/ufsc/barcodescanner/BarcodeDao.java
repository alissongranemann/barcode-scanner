package br.ufsc.barcodescanner;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

@Dao
public interface BarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBarcode(Barcode barcode);

    @Delete
    void deleteBarcode(Barcode barcode);

}
