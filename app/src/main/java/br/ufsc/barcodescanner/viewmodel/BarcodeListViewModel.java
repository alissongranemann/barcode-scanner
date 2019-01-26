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

    public void delete(String barcodeValue) {
        repository.delete(barcodeValue, aVoid -> storage.delete(externalStoragePath, barcodeValue));
    }

    public void setExternalStoragePath(String externalStoragePath) {
        this.externalStoragePath = externalStoragePath;
    }
}
