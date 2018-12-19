package br.ufsc.barcodescanner;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ItemListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final String[] barcodeValues;

    //to store the list of countries
    private final String[] imgCounts;

    public ItemListAdapter(Activity context, String[] barcodeValues, String[] imgCounts) {
        super(context, R.layout.fragment_item_list_row, barcodeValues);

        this.context = context;
        this.barcodeValues = barcodeValues;
        this.imgCounts = imgCounts;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fragment_item_list_row, null, true);

        TextView barcodeValueTextField = rowView.findViewById(R.id.barcodeValue);
        TextView imgCountTextField = rowView.findViewById(R.id.imgCount);

        barcodeValueTextField.setText(barcodeValues[position]);
        imgCountTextField.setText(imgCounts[position] + " imagens.");

        return rowView;

    }

    ;

}
