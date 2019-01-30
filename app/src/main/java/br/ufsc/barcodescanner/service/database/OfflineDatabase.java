package br.ufsc.barcodescanner.service.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.ufsc.barcodescanner.service.database.dao.GroupDao;
import br.ufsc.barcodescanner.service.database.dao.SubgroupDao;
import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.model.Subgroup;

@Database(entities = {Group.class, Subgroup.class}, version = 1)
public abstract class OfflineDatabase extends RoomDatabase {

    public static String DATABASE_NAME = "barcode_db";

    public abstract GroupDao groupDao();

    public abstract SubgroupDao subgroupDao();

}
