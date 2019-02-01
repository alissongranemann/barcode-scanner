package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Subgroup;

@Dao
public interface SubgroupDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM food_subgroup WHERE description_filter like :groupFilter and group_id=:groupId")
    List<Subgroup> getSubgroups(String groupFilter, long groupId);

}
