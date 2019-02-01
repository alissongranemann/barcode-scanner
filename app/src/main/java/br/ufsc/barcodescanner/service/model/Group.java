package br.ufsc.barcodescanner.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "food_group")
public class Group {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "description")
    public String description;

    @Override
    public String toString() {
        return description;
    }

}
