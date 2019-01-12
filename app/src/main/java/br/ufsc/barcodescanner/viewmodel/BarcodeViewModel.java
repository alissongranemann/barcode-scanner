package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;

public class BarcodeViewModel extends ViewModel {


    private static final int PAGE_LENGTH = 10;
    private MutableLiveData<List<Barcode>> barcodes;

    private BarcodeRepository repository;

    public BarcodeViewModel(BarcodeRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<List<Barcode>> getBarcodes() {
        if(barcodes == null) {
            barcodes = new MutableLiveData<>();
            reload();
        }
        return barcodes;
    }

    public void reload() {
        if(barcodes != null) {
            List<Barcode> barcodes = repository.loadBarcodes(new Date(), PAGE_LENGTH);
            this.barcodes.setValue(barcodes);
        }
    }

    public void delete(Barcode barcode) {
        this.delete(barcode.barcodeValue);
        reload();
    }

    public void insert(String barcodeValue, String uuid) {
        AsyncTask<String, Void, Void> insertTask = new InsertTask();
        insertTask.execute(barcodeValue, uuid);
    }

    public boolean hasBarcode(String barcodeValue) {
        Barcode barcode = repository.fetchBarcode(barcodeValue);
        return barcode != null;
    }

    public void delete(String barcodeValue) {
        AsyncTask<String, Void, Void> deleteTask = new DeleteTask();
        deleteTask.execute(barcodeValue);
    }

    public void startSync() {
    }

    private class InsertTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            repository.saveBarcode(strings[0], strings[1]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reload();
        }
    }

    private class DeleteTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            repository.delete(strings[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reload();
        }
    }

}
