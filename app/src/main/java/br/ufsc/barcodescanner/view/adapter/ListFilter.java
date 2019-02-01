package br.ufsc.barcodescanner.view.adapter;

import android.util.Log;
import android.widget.Filter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;

public abstract class ListFilter<T> extends Filter {

    private static final String TAG = "ListFilter";
    protected LocalDatabaseRepository repository;
    private SpinnerAdapter adapter;
    private Object lock = new Object();

    public ListFilter(LocalDatabaseRepository repository) {
        this.repository = repository;
    }

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
            List<T> matchValues = null;
            try {
                matchValues = this.getValues(filter);
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

    protected abstract List<T> getValues(String filter) throws ExecutionException, InterruptedException;

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.values != null) {
            adapter.updateGroupList((List) results.values);
        } else {
            adapter.updateGroupList(null);
        }
    }

    public void setAdapter(SpinnerAdapter adapter) {
        this.adapter = adapter;
    }

}