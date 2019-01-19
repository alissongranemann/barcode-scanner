package br.ufsc.barcodescanner.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.ufsc.barcodescanner.service.model.PictureSource;

public class PictureViewModel extends ViewModel {

    private MutableLiveData<List<PictureSource>> pictures;
    private String currentPhotoPath;
    private String picturesDirPath;

    public MutableLiveData<List<PictureSource>> getPictures() {
        if (pictures == null) {
            pictures = new MutableLiveData<>();
            pictures.setValue(new LinkedList<>());
        }
        return pictures;
    }

    public void setPicturesDirPath(String externalDirPath, String barcodePath) {
        this.picturesDirPath = externalDirPath + File.separator + barcodePath;
    }

    public boolean hasPictures() {
        return this.pictures.getValue().isEmpty();
    }

    public void clearPictureDir() {
        File dir = new File(picturesDirPath);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
            dir.delete();
        }
        this.pictures.setValue(new ArrayList<>());
    }

    public void createPicture() {
        PictureSource pictureSource = new PictureSource(currentPhotoPath);
        List<PictureSource> value = pictures.getValue();
        value.add(pictureSource);
        this.pictures.setValue(value);
    }

    public File createImageFile(String barcodeValue) throws IOException {
        String count = Integer.toString(this.pictures.getValue().size() + 1);
        String imageFileName = barcodeValue + "_" + count;
        File storageDir = new File(picturesDirPath);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        this.currentPhotoPath = image.getAbsolutePath();

        return image;
    }

}
