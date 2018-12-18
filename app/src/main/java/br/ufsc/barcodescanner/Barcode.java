package br.ufsc.barcodescanner;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "barcode")
public class Barcode {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "barcode_value")
    public String barcodeValue;


}
