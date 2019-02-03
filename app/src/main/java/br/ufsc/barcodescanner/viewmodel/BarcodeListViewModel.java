package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;

public class BarcodeListViewModel extends AbstractBarcodeViewModel implements BarcodeListObserver {

    private MutableLiveData<List<Barcode>> barcodeList;

    public BarcodeListViewModel() {
        super();
        repository.setChildEventListener(new BarcodeChildEventListener(this));
    }

    public MutableLiveData<List<Barcode>> getBarcodeList() {
        if (barcodeList == null) {
            barcodeList = new MutableLiveData<>();
            reload();
        }
        return barcodeList;
    }

    public void reload() {
        repository.loadPage(page -> barcodeList.setValue(page));
    }

    public void delete(Barcode barcode) {
        repository.delete(barcode.value, aVoid -> storage.delete(barcode));
    }

    public void setExternalStoragePath(String externalStoragePath) {
        this.externalStoragePath = externalStoragePath;
    }
}
