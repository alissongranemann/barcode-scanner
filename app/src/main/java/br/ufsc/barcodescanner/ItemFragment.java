package br.ufsc.barcodescanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Date;

import br.ufsc.barcodescanner.database.DatabaseHelper;
import br.ufsc.barcodescanner.entity.Barcode;
import br.ufsc.barcodescanner.repository.BarcodeRepository;

public class ItemFragment extends Fragment implements FragmentLifecycle {

    private static final String TAG = "CameraPreview";

    private BarcodeRepository repository;

    private ListView listView;
    private View emptyView;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new BarcodeRepository(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_list, container, false);
        listView = v.findViewById(R.id.listView);
        emptyView = v.findViewById(R.id.empty_message);
        
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated()");

        Barcode[] barcodes = repository.loadBarcodes();
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
