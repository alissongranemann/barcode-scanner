package br.ufsc.barcodescanner.view.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;

public class ItemListAdapter extends ArrayAdapter<Barcode> {

    private Activity context;
    private List<Barcode> barcodes;

    public ItemListAdapter(Activity context, List<Barcode> barcodes) {
        super(context, R.layout.fragment_item_list_row, barcodes);
        this.context = context;
        this.barcodes = barcodes;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fragment_item_list_row, null, true);

        Barcode barcode = barcodes.get(position);

        TextView barcodeValueTextField = rowView.findViewById(R.id.barcodeValue);
        TextView imgCountTextField = rowView.findViewById(R.id.imgCount);

        barcodeValueTextField.setText(barcode.barcodeValue);
        imgCountTextField.setText(TimestampConverter.dateToTimestamp(barcode.createDate));

        return rowView;

    }

}
