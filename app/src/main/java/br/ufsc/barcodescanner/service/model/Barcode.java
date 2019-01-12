package br.ufsc.barcodescanner.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.firebase.database.Exclude;

import java.util.Date;

import br.ufsc.barcodescanner.utils.TimestampConverter;

@Entity(tableName = "barcode",
        indices = {
                @Index(name = "barcode_index", value = {"value"})
        })
public class Barcode {

    @Exclude
    @PrimaryKey(autoGenerate = true)
    public int id;

    @Exclude
    @ColumnInfo(name = "value")
    public String value;

    @ColumnInfo(name = "created_at")
    @TypeConverters({TimestampConverter.class})
    public Date c;

    @ColumnInfo(name = "user_uuid")
    public String u;

    @Exclude
    @ColumnInfo(name = "synced")
    public boolean synced;

}
