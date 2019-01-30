package br.ufsc.barcodescanner.service.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "food_subgroup",
        indices = {
                @Index(name = "food_subgroup_ix_description",
                        value = {"description_filter"})
        },
        foreignKeys = {
                @ForeignKey(entity = Group.class,
                        parentColumns = "id",
                        childColumns = "group_id",
                        onDelete = CASCADE)
        }
)
public class Subgroup {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "description_filter")
    public String descriptionFilter;

    @ColumnInfo(name = "group_id")
    public int groupId;

    public static Subgroup[] populateData() {
        return new Subgroup[]{};
    }
}
