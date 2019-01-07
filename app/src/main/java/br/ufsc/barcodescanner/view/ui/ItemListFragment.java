package br.ufsc.barcodescanner.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.view.FragmentLifecycle;
import br.ufsc.barcodescanner.view.adapter.ItemListAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ItemListFragment extends Fragment implements FragmentLifecycle {

    private static final String TAG = "CameraPreview";

    private ListView listView;
    private View emptyView;
    private ItemListAdapter adapter;

    private BarcodeViewModel viewModel;

    public ItemListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this.getActivity()).get(BarcodeViewModel.class);
        viewModel.setRepository(new BarcodeRepository(this.getActivity()));
        viewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        listView = v.findViewById(R.id.listView);
        emptyView = v.findViewById(R.id.empty_message);
        //TODO: remove item from view
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemListFragment.this.getActivity());
            builder.setMessage(R.string.dialog_delete_item_message)
                    .setPositiveButton(R.string.ok, (dialog, id1) -> {
                        viewModel.delete(position);
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id12) -> {
                        // Do nothing
                    }).show();

            return true;
        });

        FloatingActionButton fab = v.findViewById(R.id.sync_fab);
        fab.setOnClickListener(view -> this.syncList());

        return v;
    }

    //TODO:
    private void syncList() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getBarcodes().observe(this, barcodes -> {
            refreshList(barcodes);
        });
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        viewModel.reload();
        refreshList(this.viewModel.getBarcodes().getValue());
    }

    private void refreshList(List<Barcode> barcodes) {
        this.adapter = new ItemListAdapter(getActivity(), barcodes);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
    }

}
