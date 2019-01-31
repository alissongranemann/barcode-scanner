package br.ufsc.barcodescanner.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "food_subgroup",
        foreignKeys = {
                @ForeignKey(entity = Group.class, parentColumns = "id", childColumns = "group_id",
                        onDelete = CASCADE)
        },
        indices = {
                @Index(name = "food_subgroup_ix_description_group",
                        value = {"description_filter", "group_id"})
        },
        primaryKeys = {"id", "group_id"}
)
public class Subgroup implements GroupEntity {

    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "description_filter")
    public String descriptionFilter;

    @ColumnInfo(name = "group_id")
    public int groupId;

    @Override
    public String getDescription() {
        return description;
    }
}
