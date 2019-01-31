package br.ufsc.barcodescanner.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.List;

import br.ufsc.barcodescanner.service.model.GroupEntity;

public class GroupListAdapter<T extends GroupEntity> extends ArrayAdapter {

    private static final String TAG = "GroupListAdapter";
    private List<T> groupList;
    private int itemLayout;

    private Filter listFilter;

    public GroupListAdapter(Context context, int resource, List<T> groupList, Filter filter) {
        super(context, resource, groupList);
        this.groupList = groupList;
        itemLayout = resource;
        this.listFilter = filter;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public T getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView group = view.findViewById(android.R.id.text1);
        group.setText(getItem(position).getDescription());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public void setGroupList(List<T> groupList) {
        this.groupList = groupList;
    }
}