package br.ufsc.barcodescanner.view.adapter;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;

import br.ufsc.barcodescanner.R;
import br.ufsc.barcodescanner.service.model.PictureSource;
import br.ufsc.barcodescanner.view.barcode.item.ScannedItemActivity;
import br.ufsc.barcodescanner.view.barcode.picture.PictureDetailFragment;

public class PictureListViewAdapter extends RecyclerView.Adapter<PictureListViewAdapter.ViewHolder> {

    private static final String TAG = "PictureListViewAdapter";

    private ArrayList<PictureSource> galleryList;
    private ScannedItemActivity activity;

    public PictureListViewAdapter(ScannedItemActivity activity, ArrayList<PictureSource> galleryList) {
        this.galleryList = galleryList;
        this.activity = activity;
    }

    @Override
    public PictureListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_list_cell, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureListViewAdapter.ViewHolder viewHolder, int position) {
        ImageView imageView = viewHolder.img;
        ImageView addButton = viewHolder.addButton;
        if(position > galleryList.size() - 1) {
            addButton.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            File file = new File(galleryList.get(position).getImageLocation());
            Uri imageUri = Uri.fromFile(file);
            Glide.with(activity.getApplicationContext())
                    .load(imageUri)
                    .fitCenter()
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_placeholder)
                    .listener(new RequestListener<Uri, GlideDrawable>() {

                        @Override
                        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Error loading image: " + e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }

                    })
                    .into(viewHolder.img);
        }
    }

    @Override
    public int getItemCount() {
        return galleryList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView img;
        private ImageButton addButton;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.img);
            img.setOnClickListener(this);
            img.setOnLongClickListener(this);

            addButton = view.findViewById(R.id.add_photo);
            addButton.setOnClickListener(new AddPictureClickListener());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PictureSource pictureSource = galleryList.get(position);
                PictureDetailFragment detailFragment = new PictureDetailFragment();
                Bundle bundle = new Bundle();

                bundle.putParcelable(PictureDetailFragment.EXTRA_SPACE_PHOTO, pictureSource);
                detailFragment.setArguments(bundle);

                FragmentManager manager = activity.getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.scanned_item, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PictureSource pictureSource = galleryList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.dialog_delete_img_message)
                        .setPositiveButton(R.string.positive, (dialog, id) -> {
                            File img = new File(pictureSource.getImageLocation());
                            if (img.exists()) {
                                img.delete();
                                galleryList.remove(position);
                                PictureListViewAdapter.this.notifyItemRangeRemoved(position, 1);
                            }
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            // Do nothing
                        }).show();
                return true;
            }
            return false;
        }
    }

    private class AddPictureClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            PictureListViewAdapter.this.activity.dispatchTakePictureIntent();
        }
    }

}