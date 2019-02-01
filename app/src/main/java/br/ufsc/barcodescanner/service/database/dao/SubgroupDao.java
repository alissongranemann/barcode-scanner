package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Subgroup;

@Dao
public interface SubgroupDao {

    @Query("SELECT * FROM food_subgroup WHERE group_id=:groupId")
    List<Subgroup> getSubgroups(long groupId);

}
