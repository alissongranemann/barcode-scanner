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

    public List<Group> loadGroups() {
        List<Group> groups = null;
        try {
            groups = new LoadGroupAsyncTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public List<Subgroup> loadSubgroups(int groupId) {
        List<Subgroup> subgroups = null;
        try {
            subgroups = new LoadSubgroupAsyncTask().execute(groupId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return subgroups;
    }

    private class LoadGroupAsyncTask extends AsyncTask<String, Void, List<Group>> {

        @Override
        protected List<Group> doInBackground(String... strings) {
            return database.groupDao().getGroups();
        }

    }

    private class LoadSubgroupAsyncTask extends AsyncTask<Integer, Void, List<Subgroup>> {

        @Override
        protected List<Subgroup> doInBackground(Integer... integers) {
            return database.subgroupDao().getSubgroups(integers[0]);
        }

    }

}
