package br.ufsc.barcodescanner.view.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.PictureSource;

public class PictureDetailFragment extends Fragment {

    public static final String EXTRA_SPACE_PHOTO = "PictureDetailFragment.SPACE_PHOTO";
    private static final String TAG = "PictureDetailFragment";
    private PictureSource source;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        source = bundle.getParcelable(EXTRA_SPACE_PHOTO);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        Toolbar toolbar = view.findViewById(R.id.image_detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Log.d(TAG, "goBack");
                this.getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setPicture(view);
    }

    private void setPicture(View view) {
        ImageView mImageView = view.findViewById(R.id.image);

        Glide.with(this)
                .load(source.getImageLocation())
                .asBitmap()
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_placeholder)
                .into(mImageView);
    }

}
