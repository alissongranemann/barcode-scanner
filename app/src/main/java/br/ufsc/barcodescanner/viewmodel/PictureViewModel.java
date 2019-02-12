package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.ufsc.barcodescanner.service.model.PictureSource;
import br.ufsc.barcodescanner.utils.FileUtils;

public class PictureViewModel extends ViewModel {

    private static final String TAG = "PictureViewModel";
    private MutableLiveData<List<PictureSource>> pictures;
    private String currentPhotoPath;
    private String externalStoragePath;

    public MutableLiveData<List<PictureSource>> getPictures() {
        if (pictures == null) {
            pictures = new MutableLiveData<>();
            pictures.setValue(new LinkedList<>());
        }
        return pictures;
    }

    public void setExternalStoragePath(String externalStoragePath) {
        this.externalStoragePath = externalStoragePath;
    }

    public boolean hasPictures() {
        return this.pictures.getValue().isEmpty();
    }


    public void clearPictureDir(String barcodeValue) {
        FileUtils.clearDir(new File(externalStoragePath, barcodeValue));
        this.pictures.setValue(new ArrayList<>());
    }

    public void createPicture() {
        PictureSource pictureSource = new PictureSource(currentPhotoPath);
        List<PictureSource> value = pictures.getValue();
        value.add(pictureSource);
        this.pictures.setValue(value);
    }

    public File createImageFile(String barcode) throws IOException {
        final int index = this.pictures.getValue().size() + 1;
        final String filename = String.format("%s_%d.jpg", barcode, index);
        File barcodeDir = new File(externalStoragePath, barcode);
        if (!barcodeDir.exists()) {
            barcodeDir.mkdir();
        }
        File image = new File(barcodeDir, filename);
        image.createNewFile();

        this.currentPhotoPath = image.getAbsolutePath();

        return image;
    }

}
