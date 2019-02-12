package br.ufsc.barcodescanner.service.model;


import java.util.HashMap;
import java.util.Map;

public class Barcode {

    public String user_uuid;

    public long created_at;

    public int group;

    public int subgroup;

    public String value;

    public Map<String, String> images;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("uid", user_uuid);
        values.put("dt", created_at);
        values.put("grp", group);
        values.put("sgp", subgroup);
        values.put("img", images);

        return values;
    }

}
