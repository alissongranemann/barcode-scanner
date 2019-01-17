package br.ufsc.barcodescanner.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BarcodeListAdapterDataObserver extends RecyclerView.AdapterDataObserver {

    private View emptyView;
    private RecyclerView.Adapter adapter;

    public BarcodeListAdapterDataObserver(View emptyView, RecyclerView.Adapter adapter) {
        this.emptyView = emptyView;
        this.adapter = adapter;
    }

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

}
