package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Group;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM food_group WHERE description_filter like :groupFilter")
    List<Group> getGroups(String groupFilter);

    @Query("SELECT * FROM food_group")
    List<Group> getGroups();
}

