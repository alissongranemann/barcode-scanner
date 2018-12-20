package br.ufsc.barcodescanner.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.entity.Barcode;
import br.ufsc.barcodescanner.repository.BarcodeRepository;

public class ItemFragment extends Fragment implements FragmentLifecycle {

    private static final String TAG = "CameraPreview";

    private BarcodeRepository repository;

    private ListView listView;
    private View emptyView;
    private Barcode[] barcodes;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new BarcodeRepository(this.getActivity());
        this.barcodes = repository.loadBarcodes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        listView = v.findViewById(R.id.listView);
        emptyView = v.findViewById(R.id.empty_message);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemFragment.this.getActivity());
                builder.setMessage(R.string.dialog_delete_item_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Barcode barcode = barcodes[position];
                                repository.delete(barcode);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing
                            }
                        }).show();

                return true;
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");

        ItemListAdapter adapter = new ItemListAdapter(getActivity(), barcodes);
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }
}
