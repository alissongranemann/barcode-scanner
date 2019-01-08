package br.ufsc.barcodescanner.view;

import br.ufsc.barcodescanner.service.model.Barcode;

public interface OnItemLongClickListener {
    void onItemClick(Barcode item);
}