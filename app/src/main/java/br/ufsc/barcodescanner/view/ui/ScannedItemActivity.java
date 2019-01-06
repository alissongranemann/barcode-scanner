package br.ufsc.barcodescanner.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.ImageSource;
import br.ufsc.barcodescanner.service.repository.BarcodeRepository;
import br.ufsc.barcodescanner.view.adapter.ImageListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeViewModel;

public class ScannedItemActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "ScannedItemActivity";

    private String barcodeValue;
    private String currentPhotoPath;
    private BarcodeViewModel viewModel;

    private ImageListViewAdapter adapter;
    private ArrayList<ImageSource> imageSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_item);

        Toolbar myToolbar = findViewById(R.id.scanned_item_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(ScannerFragment.BARCODE_VALUE);
        this.barcodeValue = message;
        //TODO: check if this barcode is already saved
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.barcode_value);
        textView.setText(message);

        viewModel = ViewModelProviders.of(this).get(BarcodeViewModel.class);
        viewModel.setRepository(new BarcodeRepository(this));
        viewModel.init();

        RecyclerView recyclerView = findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        this.imageSources = prepareData();
        this.adapter = new ImageListViewAdapter(getApplicationContext(), imageSources);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.take_photo_fab);
        fab.setOnClickListener(view -> ScannedItemActivity.this.dispatchTakePictureIntent());
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scanned_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                //TODO save item
                Toast.makeText(getApplicationContext(), getString(R.string.item_saved),
                        Toast.LENGTH_SHORT).show();
                viewModel.insert(this.barcodeValue);
                super.onBackPressed();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!this.imageSources.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.dialog_exit_message)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + barcodeValue);
                            if (dir.isDirectory()) {
                                String[] children = dir.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(dir, children[i]).delete();
                                }
                            }
                            ScannedItemActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Do nothing
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    private ArrayList<ImageSource> prepareData() {
        ArrayList<ImageSource> images = new ArrayList<>();

        File f = getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + barcodeValue);
        File file[] = f.listFiles();
        //TODO leave like this or overwrite? if doesn't overwrite this can't be deleted at onBackPressed
        for (int i = 0; i < file.length; i++) {
            ImageSource imageSource = new ImageSource(file[i].getAbsolutePath(), i);
            images.add(imageSource);
        }

        return images;
    }

    private void dispatchTakePictureIntent() {
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
        String count = String.valueOf(imageSources.size() + 1);
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
            int index = imageSources.size();
            ImageSource imageSource = new ImageSource(currentPhotoPath, index);
            imageSources.add(imageSource);

            adapter.notifyItemInserted(index);
        }
    }

}
