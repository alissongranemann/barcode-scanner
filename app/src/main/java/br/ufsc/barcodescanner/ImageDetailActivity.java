package br.ufsc.barcodescanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SPACE_PHOTO = "ImageDetailActivity.SPACE_PHOTO";
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        mImageView = findViewById(R.id.image);
        ImageSource spacePhoto = getIntent().getParcelableExtra(EXTRA_SPACE_PHOTO);

        Glide.with(this)
                .load(spacePhoto.getImageLocation())
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_placeholder)
                .into(mImageView);
    }
}
