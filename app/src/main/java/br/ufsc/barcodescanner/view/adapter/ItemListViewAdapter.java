package br.ufsc.barcodescanner.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;
import br.ufsc.barcodescanner.view.OnItemLongClickListener;

public class ItemListViewAdapter extends RecyclerView.Adapter<ItemListViewAdapter.ViewHolder> {

    private List<Barcode> barcodes;
    private OnItemLongClickListener onItemLongClickListener;

    public ItemListViewAdapter(List<Barcode> barcodes, OnItemLongClickListener onItemLongClickListener) {
        this.barcodes = barcodes;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item_list_row, viewGroup, false);
        return new ItemListViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Barcode barcode = barcodes.get(i);
        viewHolder.barcodeValue.setText(barcode.barcodeValue);
        viewHolder.imgCount.setText(TimestampConverter.dateToTimestamp(barcode.createDate));
        viewHolder.bind(barcode, onItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return barcodes.size();
    }

    public void setBarcodes(List<Barcode> barcodes) {
        this.barcodes = barcodes;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView barcodeValue;
        public TextView imgCount;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.barcodeValue = view.findViewById(R.id.barcodeValue);
            this.imgCount = view.findViewById(R.id.imgCount);
        }

        public void bind(Barcode barcode, OnItemLongClickListener onItemLongClickListener) {
            this.view.setOnLongClickListener(v -> {
                onItemLongClickListener.onItemClick(barcode);
                return true;
            });
        }
    }

}