package br.ufsc.barcodescanner.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import br.ufsc.barcodescanner.utils.TimestampConverter;

@Entity(tableName = "barcode")
public class Barcode {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "barcode_value")
    public String barcodeValue;

    @ColumnInfo(name = "created_date")
    @TypeConverters({TimestampConverter.class})
    public Date createDate;

}
