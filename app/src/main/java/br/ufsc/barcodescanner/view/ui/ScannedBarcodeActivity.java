package br.ufsc.barcodescanner.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.Group;
import br.ufsc.barcodescanner.service.model.Subgroup;
import br.ufsc.barcodescanner.service.repository.LocalDatabaseRepository;
import br.ufsc.barcodescanner.utils.UUIDManager;
import br.ufsc.barcodescanner.view.adapter.EmptyListAdapterDataObserver;
import br.ufsc.barcodescanner.view.adapter.PictureListViewAdapter;
import br.ufsc.barcodescanner.viewmodel.BarcodeItemViewModel;
import br.ufsc.barcodescanner.viewmodel.PictureViewModel;

public class ScannedBarcodeActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "ScannedBarcodeActivity";

    private String barcodeValue;

    private BarcodeItemViewModel barcodeItemViewModel;
    private PictureViewModel pictureViewModel;
    private Spinner groupSpinner;
    private Spinner subgroupSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        setToolbar();
        createListSection();
        createGroupSection();
    }

    private void setToolbar() {
        Intent intent = getIntent();
        this.barcodeValue = intent.getStringExtra(BarcodeScannerActivity.BARCODE_VALUE);

        Toolbar myToolbar = findViewById(R.id.scanned_item_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mTitle = myToolbar.findViewById(R.id.scanned_item_toolbar_title);
        mTitle.setText(barcodeValue);
    }

    private void createGroupSection() {
        LocalDatabaseRepository repository = new LocalDatabaseRepository(this);
        List<Group> groups = repository.loadGroups();

        this.subgroupSpinner = findViewById(R.id.subgroup_spinner);
        this.groupSpinner = findViewById(R.id.group_spinner);
        ArrayAdapter<Group> groupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, groups);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) parent.getItemAtPosition(position);
                List<Subgroup> subgroups = repository.loadSubgroups(group.id);
                ArrayAdapter<Subgroup> subgroupAdapter =
                        new ArrayAdapter<>(ScannedBarcodeActivity.this,
                                android.R.layout.simple_dropdown_item_1line, subgroups);
                subgroupSpinner.setAdapter(subgroupAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void createListSection() {
        TextView emptyView = findViewById(R.id.picture_list_empty_message);
        RecyclerView recyclerView = findViewById(R.id.picture_gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        PictureListViewAdapter adapter = new PictureListViewAdapter(this);
        adapter.registerAdapterDataObserver(new EmptyListAdapterDataObserver(emptyView, adapter));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.add_photo_fab);
        fab.setOnClickListener(view -> this.dispatchTakePictureIntent());

        String picturesPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        barcodeItemViewModel = ViewModelProviders.of(this).get(BarcodeItemViewModel.class);
        barcodeItemViewModel.setExternalStoragePath(picturesPath);
        pictureViewModel = ViewModelProviders.of(this).get(PictureViewModel.class);
        pictureViewModel.setExternalStoragePath(picturesPath);
        pictureViewModel.getPictures().observe(this,
                pictureSources -> adapter.setPictures(pictureSources));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanned_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                this.saveItem();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        Toast.makeText(getApplicationContext(), getString(R.string.item_saved),
                Toast.LENGTH_SHORT).show();
        final Group group = (Group) this.groupSpinner.getSelectedItem();
        final Subgroup subgroup = (Subgroup) subgroupSpinner.getSelectedItem();
        barcodeItemViewModel.insert(this.barcodeValue, UUIDManager.id(this),
                group.id, subgroup.id);
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            if (!this.pictureViewModel.hasPictures()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_exit_message)
                        .setPositiveButton(R.string.ok, (dialog, id) -> {
                            this.pictureViewModel.clearPictureDir(this.barcodeValue);
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

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = this.pictureViewModel.createImageFile(barcodeValue);
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            getString(R.string.file_provider),
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Dispatch take picture intent failed: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Error getUriForFile: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.pictureViewModel.createPicture();
        }
    }

}