package br.ufsc.barcodescanner.ui;

import android.content.DialogInterface;
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

public class ImageDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SPACE_PHOTO = "ImageDetailActivity.SPACE_PHOTO";
    public static final String PHOTO_REMOVED_NOTIFIER = "ImageDetailActivity.PHOTO_REMOVED_NOTIFIER";
    private ImageSource spacePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar myToolbar = findViewById(R.id.image_detail_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView mImageView = findViewById(R.id.image);
        spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        Glide.with(this)
                .load(spacePhoto.getImageLocation())
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_placeholder)
                .into(mImageView);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.image_detail_action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageDetailActivity.this);
                builder.setMessage(R.string.dialog_delete_img_message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                File img = new File(spacePhoto.getImageLocation());
                                if (img.exists()) {
                                    //TODO update recycler view
                                    img.delete();
                                }
                                ImageDetailActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing
                            }
                        }).show();
                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
