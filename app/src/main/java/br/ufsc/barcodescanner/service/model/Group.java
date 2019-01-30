package br.ufsc.barcodescanner.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "food_group", indices = {
        @Index(name = "food_group_ix_description", value = {"description_filter"})
})
public class Group {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "description_filter")
    public String descriptionFilter;

}
