package br.ufsc.barcodescanner.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.utils.UUIDManager;
import br.ufsc.barcodescanner.view.OnItemLongClickListener;
import br.ufsc.barcodescanner.view.adapter.EmptyListAdapterDataObserver;
import br.ufsc.barcodescanner.view.adapter.ItemListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeListViewModel;

public class BarcodeListActivity extends AppCompatActivity implements OnItemLongClickListener {

    private static final String TAG = "CameraPreview";

    private ItemListViewAdapter adapter;
    private BarcodeListViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_list);

        viewModel = ViewModelProviders.of(this).get(BarcodeListViewModel.class);
        viewModel.getBarcodeList().observe(this, barcodeList -> refreshList(barcodeList));
        viewModel.setExternalStoragePath(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES).getAbsolutePath());

        createToolbar();
        createList();
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.barcode_list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void createList() {
        this.adapter = new ItemListViewAdapter(this);
        TextView emptyView = findViewById(R.id.barcode_list_empty_message);
        RecyclerView recyclerView = findViewById(R.id.barcodeList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new EmptyListAdapterDataObserver(emptyView, adapter));
    }

    private void refreshList(List<Barcode> barcodeList) {
        adapter.setBarcodes(barcodeList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Barcode item) {
        final String uuid = UUIDManager.id(this);
        if (item.user_uuid != null && item.user_uuid.compareTo(uuid) == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_delete_item_message)
                    .setPositiveButton(R.string.positive, (dialog, id1) -> {
                        viewModel.delete(item);
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id12) -> {
                        // Do nothing
                    }).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.denied_delete_message)
                    .setNeutralButton(R.string.ok,
                            (dialog, id1) -> {
                                // Do nothing
                            }).show();
        }
    }

}
