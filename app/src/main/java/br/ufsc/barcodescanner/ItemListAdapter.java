package br.ufsc.barcodescanner;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.ufsc.barcodescanner.entity.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;

public class ItemListAdapter extends ArrayAdapter<Barcode> {

    private final Activity context;
    private final Barcode[] barcodes;

    public ItemListAdapter(Activity context, Barcode[] barcodes) {
        super(context, R.layout.fragment_item_list_row, barcodes);

        this.context = context;
        this.barcodes = barcodes;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fragment_item_list_row, null, true);

        Barcode barcode = barcodes[position];

        TextView barcodeValueTextField = rowView.findViewById(R.id.barcodeValue);
        TextView imgCountTextField = rowView.findViewById(R.id.imgCount);

        barcodeValueTextField.setText(barcode.barcodeValue);
        imgCountTextField.setText(TimestampConverter.dateToTimestamp(barcode.createDate));

        return rowView;

    }

    ;

}
