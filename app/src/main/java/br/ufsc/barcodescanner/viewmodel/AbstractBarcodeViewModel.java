package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.ViewModel;

import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;
import br.ufsc.barcodescanner.service.repository.FirebasePictureStorage;

public abstract class AbstractBarcodeViewModel extends ViewModel {

    protected FirebaseBarcodeRepository repository;
    protected FirebasePictureStorage storage;
    protected String externalStoragePath;

    public AbstractBarcodeViewModel() {
        this.repository = new FirebaseBarcodeRepository();
        this.storage = new FirebasePictureStorage();
    }

    public void setExternalStoragePath(String externalStoragePath) {
        this.externalStoragePath = externalStoragePath;
    }

}
