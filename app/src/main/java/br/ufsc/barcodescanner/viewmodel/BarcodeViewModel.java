package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.service.repository.FirebaseBarcodeRepository;

public class BarcodeViewModel extends ViewModel {


    private static final int PAGE_LENGTH = 10;
    private FirebaseBarcodeRepository firebaseRepository;
    private MutableLiveData<List<Barcode>> barcodes;

    private BarcodeRepository repository;

    public BarcodeViewModel(BarcodeRepository repository) {
        this.repository = repository;
        this.firebaseRepository = new FirebaseBarcodeRepository();
    }

    public MutableLiveData<List<Barcode>> getBarcodes() {
        if (barcodes == null) {
            barcodes = new MutableLiveData<>();
            reload();
        }
        return barcodes;
    }

    public void reload() {
        if (barcodes != null) {
            List<Barcode> barcodes = repository.loadPage(new Date(), PAGE_LENGTH);
            this.barcodes.setValue(barcodes);
        }
    }

    public void delete(Barcode barcode) {
        this.delete(barcode.value);
        reload();
    }

    public void insert(String barcodeValue, String uuid) {
        AsyncTask<String, Void, Void> insertTask = new InsertTask();
        insertTask.execute(barcodeValue, uuid);
    }

    public boolean hasBarcode(String barcodeValue) {
        return repository.hasBarcode(barcodeValue);
    }

    public void delete(String barcodeValue) {
        AsyncTask<String, Void, Void> deleteTask = new DeleteTask();
        deleteTask.execute(barcodeValue);
    }

    public void startSync() {
        Date lastDate = new Date();
        List<Barcode> barcodes;
        do {
            barcodes = repository.loadPage(lastDate, PAGE_LENGTH);
            for (Barcode barcode : barcodes) {
                this.firebaseRepository.save(barcode.value, barcode.userUuid);
                lastDate = barcode.createdAt;
            }
        } while (barcodes.size() == PAGE_LENGTH);
    }

    private class InsertTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            repository.save(strings[0], strings[1]);

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
