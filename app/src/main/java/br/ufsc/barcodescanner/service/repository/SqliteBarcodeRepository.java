package br.ufsc.barcodescanner.service.repository;

import android.content.Context;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.database.AppSqliteDatabase;
import br.ufsc.barcodescanner.service.database.SqliteDatabase;
import br.ufsc.barcodescanner.service.model.Barcode;
import br.ufsc.barcodescanner.utils.TimestampConverter;

public class SqliteBarcodeRepository implements BarcodeRepository {

    private AppSqliteDatabase database;

    public SqliteBarcodeRepository(Context context) {
        this.database = SqliteDatabase.getInstance(context);
    }

    public List<Barcode> loadPage(Date lastDate, int pageLength) {
        String now = TimestampConverter.dateToTimestamp(lastDate);
        return database.itemDao().loadPage(now, pageLength);
    }

    public void save(String barcodeValue, String uuid) {
        Barcode barcode = new Barcode();
        barcode.value = barcodeValue;
        barcode.createdAt = new Date();
        barcode.userUuid = uuid;
        barcode.synced = false;
        database.itemDao().insert(barcode);
    }

    public boolean hasBarcode(String barcodeValue) {
        return database.itemDao().fetch(barcodeValue) != null;
    }

    public void delete(String barcodeValue) {
        this.database.itemDao().delete(barcodeValue);
    }
}
