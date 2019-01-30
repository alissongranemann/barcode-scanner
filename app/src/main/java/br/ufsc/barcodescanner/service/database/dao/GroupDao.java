package br.ufsc.barcodescanner.service.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Group;

@Dao
public interface GroupDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, description FROM food_group WHERE description_filter like :groupFilter")
    List<Group> getGroups(String groupFilter);

}

