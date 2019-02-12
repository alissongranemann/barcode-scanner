package br.ufsc.barcodescanner.service.model;

import java.util.Collections;
import java.util.Map;

public class PictureUrl {

    public String filename;

    public String url;

    public PictureUrl(String filename, String url) {
        this.filename = filename;
        this.url = url;
    }

    public Map<String, String> toMap() {
        return Collections.singletonMap(filename, url);
    }

}
