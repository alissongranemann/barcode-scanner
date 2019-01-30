package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.model.Subgroup;

@Dao
public interface SubgroupDao {

    @Insert
    void insertAll(Subgroup... entities);

    @Query("SELECT * FROM food_subgroup WHERE description_filter like :groupFilter and group_id=:groupId")
    List<Group> getSubgroups(String groupFilter, long groupId);

}
