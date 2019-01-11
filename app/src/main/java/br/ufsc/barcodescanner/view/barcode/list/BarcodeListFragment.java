package br.ufsc.barcodescanner.view.barcode.list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.utils.ViewModelFactory;
import br.ufsc.barcodescanner.view.FragmentLifecycle;
import br.ufsc.barcodescanner.view.OnItemLongClickListener;
import br.ufsc.barcodescanner.view.adapter.ItemListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class BarcodeListFragment extends Fragment implements FragmentLifecycle, OnItemLongClickListener {

    private static final String TAG = "CameraPreview";

    private ItemListViewAdapter adapter;
    private BarcodeViewModel viewModel;

    public BarcodeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelFactory viewModelFactory = new ViewModelFactory(new BarcodeRepository(this.getActivity()));
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BarcodeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);

        createList(v);
        createSyncButton(v);

        return v;
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

    private void createList(View view) {
        this.adapter = new ItemListViewAdapter(viewModel.getBarcodes().getValue(), this);
        TextView emptyView = view.findViewById(R.id.empty_message);
        RecyclerView recyclerView = view.findViewById(R.id.barcodeList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new BarcodeListAdapterDataObserver(emptyView, adapter));
    }

    private void createSyncButton(View v) {
        FloatingActionButton fab = v.findViewById(R.id.sync_fab);
        fab.setOnClickListener(view -> this.syncList());
    }

    //TODO: sync
    private void syncList() {
        this.viewModel.startSync();
    }

    private void refreshList(List<Barcode> barcodes) {
        adapter.setBarcodes(barcodes);
    }

    @Override
    public void onItemClick(Barcode item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeListFragment.this.getActivity());
        builder.setMessage(R.string.dialog_delete_item_message)
                .setPositiveButton(R.string.positive, (dialog, id1) -> {
                    viewModel.delete(item);
                })
                .setNegativeButton(R.string.cancel, (dialog, id12) -> {
                    // Do nothing
                }).show();
    }
}
