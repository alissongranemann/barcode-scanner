package br.ufsc.barcodescanner.view.adapter;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Filter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;

public class SubgroupListFilter extends Filter {

    private static final String TAG = "SubgroupListFilter";

    private GroupListAdapter adapter;
    private int groupId = -1;

    public SubgroupListFilter(LocalDatabaseRepository repository) {
        this.repository = repository;
    }

    private LocalDatabaseRepository repository;

    private Object lock = new Object();

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();

        if (prefix == null || prefix.length() == 0) {
            synchronized (lock) {
                results.values = new ArrayList<String>();
                results.count = 0;
            }
        } else {
            final String filter = StringUtils.stripAccents(prefix.toString().toLowerCase());
            List<Group> matchValues = null;
            try {
                matchValues = new LoadGroupAsyncTask().execute(filter).get();
            } catch (ExecutionException e) {
                Log.e(TAG, "Load groups execution exception: " + e.getMessage());
            } catch (InterruptedException e) {
                Log.e(TAG, "Load groups interrupted exception: " + e.getMessage());
            }

            results.values = matchValues;
            results.count = matchValues.size();
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values != null) {
           adapter.setGroupList((List) results.values);
        } else {
            adapter.setGroupList(null);
        }
        if (results.count > 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyDataSetInvalidated();
        }
    }

    public void setAdapter(GroupListAdapter adapter) {
        this.adapter = adapter;
    }

    public void setGroupId(int id) {
        this.groupId = id;
    }

    private class LoadGroupAsyncTask extends AsyncTask<String, Void, List<Group>> {

        @Override
        protected List<Group> doInBackground(String... strings) {
            return repository.loadSubgroups(strings[0], groupId);
        }

        @Override
        protected void onPostExecute(List<Group> groups) {
            super.onPostExecute(groups);
        }
    }

}