package br.ufsc.barcodescanner.service.repository;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.ufsc.barcodescanner.service.database.LocalDatabase;
import br.ufsc.barcodescanner.service.database.LocalDatabaseHelper;
import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.model.Subgroup;

public class LocalDatabaseRepository {

    private LocalDatabase database;

    public LocalDatabaseRepository(Context context) {
        this.database = LocalDatabaseHelper.getInstance(context);
    }

    public List<Group> loadGroups(String filter) throws ExecutionException, InterruptedException {
        return new LoadGroupAsyncTask().execute(filter).get();
    }

    public List<Subgroup> loadSubgroups(String filter, int groupId) throws ExecutionException, InterruptedException {
        return new LoadSubgroupAsyncTask().execute(filter, String.valueOf(groupId)).get();
    }

    private static String likeable(String filter) {
        return String.format("%%%s%%", filter);
    }

    private class LoadGroupAsyncTask extends AsyncTask<String, Void, List<Group>> {

        @Override
        protected List<Group> doInBackground(String... strings) {
            return database.groupDao().getGroups(likeable(strings[0]));
        }

    }

    private class LoadSubgroupAsyncTask extends AsyncTask<String, Void, List<Subgroup>> {

        @Override
        protected List<Subgroup> doInBackground(String... strings) {
            return database.subgroupDao().getSubgroups(likeable(strings[0]), Integer.getInteger(strings[1]));
        }

    }

}
