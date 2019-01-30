package br.ufsc.barcodescanner.service.repository;

import android.content.Context;

import java.util.List;

import br.ufsc.barcodescanner.service.database.OfflineDatabase;
import br.ufsc.barcodescanner.service.database.OfflineDatabaseHelper;
import br.ufsc.barcodescanner.service.model.Group;

public class GroupRepository {

    private OfflineDatabase database;

    public GroupRepository(Context context) {
        this.database = OfflineDatabaseHelper.getInstance(context);
    }

    public List<Group> loadGroups(String descriptionFilter) {
        return database.groupDao().getGroups(descriptionFilter);
    }

    public List<Group> loadSubgroups(String descriptionFilter, long groupId) {
        return database.subgroupDao().getSubgroups(descriptionFilter, groupId);
    }
}
