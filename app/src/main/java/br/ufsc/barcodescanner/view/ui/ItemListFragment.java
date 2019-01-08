package br.ufsc.barcodescanner.view.ui;

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

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.utils.ViewModelFactory;
import br.ufsc.barcodescanner.view.FragmentLifecycle;
import br.ufsc.barcodescanner.view.OnItemLongClickListener;
import br.ufsc.barcodescanner.view.adapter.ItemListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ItemListFragment extends Fragment implements FragmentLifecycle, OnItemLongClickListener {

    private static final String TAG = "CameraPreview";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemListViewAdapter adapter;

    private BarcodeViewModel viewModel;
    private View emptyView;

    public ItemListFragment() {
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

        this.recyclerView = v.findViewById(R.id.barcodeList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        this.adapter = new ItemListViewAdapter(viewModel.getBarcodes().getValue(), this);
        recyclerView.setAdapter(adapter);
        emptyView = v.findViewById(R.id.empty_message);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }

            void checkEmpty() {
                emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        FloatingActionButton fab = v.findViewById(R.id.sync_fab);
        fab.setOnClickListener(view -> this.syncList());

        return v;
    }

    //TODO: sync
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
        adapter.setBarcodes(barcodes);
    }

    @Override
    public void onItemClick(Barcode item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ItemListFragment.this.getActivity());
        builder.setMessage(R.string.dialog_delete_item_message)
                .setPositiveButton(R.string.ok, (dialog, id1) -> {
                    viewModel.delete(item);
                })
                .setNegativeButton(R.string.cancel, (dialog, id12) -> {
                    // Do nothing
                }).show();
    }
}
