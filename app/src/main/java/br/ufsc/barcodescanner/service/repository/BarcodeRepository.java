package br.ufsc.barcodescanner.service.repository;

import android.content.Context;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.database.AppDatabase;
import br.ufsc.barcodescanner.service.database.DatabaseHelper;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;

public class BarcodeRepository {

    private AppDatabase database;

    public BarcodeRepository(Context context) {
        this.database = DatabaseHelper.getInstance(context);
    }

        public List<Barcode> loadBarcodes(Date lastDate, int pageLength) {
            String now = TimestampConverter.dateToTimestamp(lastDate);
            return database.itemDao().loadBarcodes(now, pageLength);
        }

    public void saveBarcode(String barcodeValue) {
        Barcode barcode = new Barcode();
        barcode.barcodeValue = barcodeValue;
        barcode.createDate = new Date();
        database.itemDao().insertBarcode(barcode);
    }

    public void delete(Barcode barcode) {
        this.database.itemDao().deleteBarcode(barcode);
    }
}
