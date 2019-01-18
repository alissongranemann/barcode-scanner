package br.ufsc.barcodescanner.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.PictureSource;
import br.ufsc.barcodescanner.utils.UUIDManager;
import br.ufsc.barcodescanner.view.adapter.PictureListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "ScannedBarcodeActivity";

    private String barcodeValue;
    private String currentPhotoPath;
    private BarcodeViewModel viewModel;

    private PictureListViewAdapter adapter;
    private ArrayList<PictureSource> pictureSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        Intent intent = getIntent();
        this.barcodeValue = intent.getStringExtra(BarcodeScannerActivity.BARCODE_VALUE);

        viewModel = ViewModelProviders.of(this).get(BarcodeViewModel.class);

        Toolbar myToolbar = findViewById(R.id.scanned_item_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = myToolbar.findViewById(R.id.scanned_item_toolbar_title);
        mTitle.setText(barcodeValue);

        RecyclerView recyclerView = findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        this.pictureSources = prepareData();
        this.adapter = new PictureListViewAdapter(this, pictureSources);
        recyclerView.setAdapter(adapter);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(view -> ScannedBarcodeActivity.this.saveItem());
    }

    private void saveItem() {
        Toast.makeText(getApplicationContext(), getString(R.string.item_saved),
                Toast.LENGTH_SHORT).show();
        viewModel.insert(this.barcodeValue, UUIDManager.id(this));
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            if (!this.pictureSources.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_exit_message)
                        .setPositiveButton(R.string.ok, (dialog, id) -> {
                            clearPictureDir();
                            ScannedBarcodeActivity.super.onBackPressed();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            // Do nothing
                        }).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void clearPictureDir() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + barcodeValue);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
            dir.delete();
        }
        pictureSources.clear();
    }

    private ArrayList<PictureSource> prepareData() {
        ArrayList<PictureSource> images = new ArrayList<>();

        File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + barcodeValue);
        File file[] = f.listFiles();
        for (int i = 0; i < file.length; i++) {
            PictureSource pictureSource = new PictureSource(file[i].getAbsolutePath());
            images.add(pictureSource);
        }

        return images;
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "br.ufsc.barcodescanner.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error at camera source start: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error getUriForFile: " + e.getMessage());
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String count = String.valueOf(pictureSources.size() + 1);
        String imageFileName = barcodeValue + "_" + count;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + barcodeValue);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        this.currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "Reloading recycler view.");
            int index = pictureSources.size();
            PictureSource pictureSource = new PictureSource(currentPhotoPath);
            pictureSources.add(pictureSource);

            adapter.notifyItemInserted(index);
        }
    }

}