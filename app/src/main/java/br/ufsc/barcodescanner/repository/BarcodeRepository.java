package br.ufsc.barcodescanner.repository;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.Date;

import br.ufsc.barcodescanner.database.DatabaseHelper;
import br.ufsc.barcodescanner.entity.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;
import br.ufsc.barcodescanner.database.AppDatabase;

public class BarcodeRepository {

    private AppDatabase database;

    public BarcodeRepository(Context context) {
        this.database = DatabaseHelper.getInstance(context);
    }

    public Barcode[] loadBarcodes(Date lastDate, int pageLength) {
        String now = TimestampConverter.dateToTimestamp(lastDate);
        return database.itemDao().loadBarcodes(now, pageLength);
    }

    public void saveBarcode(String barcodeValue) {
        Barcode barcode = new Barcode();
        barcode.barcodeValue = barcodeValue;
        barcode.createDate = new Date();
        database.itemDao().insertBarcode(barcode);
    }

    public Barcode[] loadBarcodes() {
        return database.itemDao().loadAllBarcodes();
    }
}
