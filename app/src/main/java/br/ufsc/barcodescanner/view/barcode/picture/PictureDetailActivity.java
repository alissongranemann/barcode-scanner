package br.ufsc.barcodescanner.view.barcode.picture;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.PictureSource;

public class PictureDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SPACE_PHOTO = "PictureDetailActivity.SPACE_PHOTO";
    private PictureSource spacePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        setToolbar();
        setPicture();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.image_detail_action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(PictureDetailActivity.this);
                builder.setMessage(R.string.dialog_delete_img_message)
                        .setPositiveButton(R.string.ok, (dialog, id) -> {
                            File img = new File(spacePhoto.getImageLocation());
                            if (img.exists()) {
                                //TODO: update recycler view
                                img.delete();
                            }
                            PictureDetailActivity.super.onBackPressed();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            // Do nothing
                        }).show();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar myToolbar = findViewById(R.id.image_detail_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setPicture() {
        ImageView mImageView = findViewById(R.id.image);
        spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        Glide.with(this)
                .load(spacePhoto.getImageLocation())
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_placeholder)
                .into(mImageView);
    }

}
