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
    void insert(Barcode barcode);

    @Delete
    void delete(Barcode barcode);

    @Query("DELETE FROM barcode WHERE value = :barcodeValue")
    void delete(String barcodeValue);

    @Query("SELECT * FROM barcode WHERE created_at > date(:lastDate) ORDER BY created_at DESC LIMIT :pageLength")
    List<Barcode> loadPage(String lastDate, int pageLength);

    @Query("SELECT * FROM barcode WHERE value = :barcodeValue")
    Barcode fetch(String barcodeValue);

}
