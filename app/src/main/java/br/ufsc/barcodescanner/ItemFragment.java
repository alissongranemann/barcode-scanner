package br.ufsc.barcodescanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ItemFragment extends Fragment {

    private ListView listView;

    public ItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] barcodeValues = new String[]{"ABCDE123"};
        String[] imgCounts = new String[]{"1"};
        ItemListAdapter adapter = new ItemListAdapter(getActivity(), barcodeValues, imgCounts);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
