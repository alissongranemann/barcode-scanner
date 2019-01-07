package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;

public class BarcodeViewModel extends ViewModel {

    private MutableLiveData<List<Barcode>> barcodes;

    private BarcodeRepository repository;

    //TODO: Inject
    //    public BarcodeViewModel(BarcodeRepository repository) {
    //        this.repository = repository;
    //    }

    public void setRepository(BarcodeRepository repository) {
        this.repository = repository;
    }

    public void init() {
        if (barcodes != null) {
            return;
        }
        barcodes = new MutableLiveData<>();
        reload();
    }

    public MutableLiveData<List<Barcode>> getBarcodes() {
        return barcodes;
    }

    public void reload() {
        barcodes.setValue(repository.loadBarcodes(new Date(), 10));
    }

    public void delete(int index) {
        List<Barcode> barcodes = getBarcodes().getValue();
        this.repository.delete(barcodes.get(index));
    }

    public void insert(String barcodeValue) {
        AsyncTask<String, Void, Void> insertTask = new InsertTask();
        insertTask.execute(barcodeValue);
    }

    public boolean hasBarcode(String barcodeValue) {
        Barcode barcode = repository.fetchBarcode(barcodeValue);
        return barcode != null;
    }

    public void delete(String barcodeValue) {
        this.repository.delete(barcodeValue);
        reload();
    }

    private class InsertTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            repository.saveBarcode(strings[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reload();
        }
    }

}
