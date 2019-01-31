package br.ufsc.barcodescanner.service.repository;

import android.content.Context;

import java.util.List;

import br.ufsc.barcodescanner.service.database.LocalDatabase;
import br.ufsc.barcodescanner.service.database.LocalDatabaseHelper;
import br.ufsc.barcodescanner.service.model.Group;

public class LocalDatabaseRepository {

    private LocalDatabase database;

    public LocalDatabaseRepository(Context context) {
        this.database = LocalDatabaseHelper.getInstance(context);
    }

    public List<Group> loadGroups(String descriptionFilter) {
        return database.groupDao().getGroups(getLikeableFilter(descriptionFilter));
    }

    public List<Group> loadSubgroups(String descriptionFilter, long groupId) {
        return database.subgroupDao().getSubgroups(getLikeableFilter(descriptionFilter), groupId);
    }

    private String getLikeableFilter(String filter) {
        return String.format("%%%s%%", filter);
    }

}
