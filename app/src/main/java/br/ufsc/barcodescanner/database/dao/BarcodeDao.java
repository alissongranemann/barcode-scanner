package br.ufsc.barcodescanner.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import br.ufsc.barcodescanner.entity.Barcode;

@Dao
public interface BarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBarcode(Barcode barcode);

    @Delete
    void deleteBarcode(Barcode barcode);

    @Query("SELECT * FROM barcode WHERE created_date > date(:lastDate) ORDER BY created_date DESC LIMIT :pageLength")
    Barcode[] loadBarcodes(String lastDate, int pageLength);

    @Query("SELECT * FROM barcode")
    Barcode[] loadAllBarcodes();

}
