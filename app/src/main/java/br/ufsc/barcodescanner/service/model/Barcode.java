package br.ufsc.barcodescanner.service.model;


import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

public class Barcode {

    // user_uuid
    public String id;

    // created_at
    public long dt;

    // group
    public int g;

    // subgroup
    public int sg;

    @Exclude
    public String value;

    public Map<String, String> img;

}
