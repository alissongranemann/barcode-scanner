package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;

@Dao
public interface BarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBarcode(Barcode barcode);

    @Delete
    void deleteBarcode(Barcode barcode);

    @Query("SELECT * FROM barcode WHERE created_date > date(:lastDate) ORDER BY created_date DESC LIMIT :pageLength")
    List<Barcode> loadBarcodes(String lastDate, int pageLength);

    @Query("SELECT * FROM barcode")
    List<Barcode> loadAllBarcodes();

    @Query("SELECT * FROM barcode WHERE barcode_value = :barcodeValue")
    Barcode fetchBarcode(String barcodeValue);

    @Query("DELETE FROM barcode WHERE barcode_value = :barcodeValue")
    void deleteBarcode(String barcodeValue);
}
