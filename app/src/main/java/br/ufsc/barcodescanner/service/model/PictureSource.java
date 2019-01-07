package br.ufsc.barcodescanner.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PictureSource implements Parcelable {

    public static final Creator<PictureSource> CREATOR = new Creator<PictureSource>() {
        @Override
        public PictureSource createFromParcel(Parcel in) {
            return new PictureSource(in);
        }

        @Override
        public PictureSource[] newArray(int size) {
            return new PictureSource[size];
        }
    };
    private String location;
    private int index;

    public PictureSource(String path, int index) {
        this.location = path;
        this.index = index;
    }

    protected PictureSource(Parcel in) {
        location = in.readString();
    }

    public String getImageLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
    }

    public int getIndex() {
        return index;
    }
}