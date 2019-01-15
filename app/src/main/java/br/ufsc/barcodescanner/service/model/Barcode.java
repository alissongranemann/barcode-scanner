package br.ufsc.barcodescanner.service.model;


import com.google.firebase.database.Exclude;

import java.util.Date;

public class Barcode {

    // user_uuid
    public String id;

    // created_at
    public Date dt;

    @Exclude
    public String value;

}
