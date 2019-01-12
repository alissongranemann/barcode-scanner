package br.ufsc.barcodescanner.service.repository;

import java.util.Date;
import java.util.List;

import br.ufsc.barcodescanner.service.model.Barcode;

public interface BarcodeRepository {

    List<Barcode> loadPage(Date lastDate, int pageLength);

    void save(String barcodeValue, String uuid);

    boolean hasBarcode(String barcodeValue);

    void delete(String barcodeValue);

}
