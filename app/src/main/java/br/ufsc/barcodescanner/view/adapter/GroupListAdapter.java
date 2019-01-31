package br.ufsc.barcodescanner.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;

public class GroupListAdapter extends ArrayAdapter {

    private List<Group> groupList;
    private Context mContext;
    private int itemLayout;

    private LocalDatabaseRepository localDatabaseRepo;

    private GroupListAdapter.ListFilter listFilter = new GroupListAdapter.ListFilter();

    public GroupListAdapter(Context context, int resource, List<Group> groupList) {
        super(context, resource, groupList);
        this.localDatabaseRepo = new LocalDatabaseRepository(context);
        this.groupList = groupList;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Group getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView group = view.findViewById(R.id.groupDescription);
        group.setText(getItem(position).description);

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
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
                final String searchStrLowerCase = StringUtils.stripAccents(prefix.toString().toLowerCase());

                //Call to database to get matching records using room
                List<Group> matchValues =
                        localDatabaseRepo.loadGroups(searchStrLowerCase);

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                groupList = (ArrayList<Group>)results.values;
            } else {
                groupList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}