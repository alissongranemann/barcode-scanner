package br.ufsc.barcodescanner.service.model;


import com.google.firebase.database.Exclude;

public class Barcode {

    // user_uuid
    public String id;

    // created_at
    public long dt;

    @Exclude
    public String value;

}
