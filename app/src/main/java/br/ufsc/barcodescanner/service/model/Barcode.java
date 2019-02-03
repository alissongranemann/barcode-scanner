package br.ufsc.barcodescanner.service.model;


import com.google.firebase.database.Exclude;

public class Barcode {

    // user_uuid
    public String id;

    // created_at
    public long dt;

    // group
    public int g;

    // subgroup
    public int sg;

    // img count
    public int ic;

    @Exclude
    public String value;

}
