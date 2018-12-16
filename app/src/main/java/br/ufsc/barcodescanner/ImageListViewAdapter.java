package br.ufsc.barcodescanner;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;

public class ImageListViewAdapter extends RecyclerView.Adapter<ImageListViewAdapter.ViewHolder> {

    private static final String TAG = "ImageListViewAdapter";

    private ArrayList<ImageSource> galleryList;
    private Context context;

    public ImageListViewAdapter(Context context, ArrayList<ImageSource> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public ImageListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageListViewAdapter.ViewHolder viewHolder, int i) {
        File file = new File(galleryList.get(i).getImageLocation());
        Uri imageUri = Uri.fromFile(file);
        final ImageView imageView = viewHolder.img;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(context)
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

//        viewHolder.img.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"Image",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public ViewHolder(View view) {
            super(view);

            img = view.findViewById(R.id.img);
        }
    }

}