package br.ufsc.barcodescanner.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageSource implements Parcelable {

    public static final Creator<ImageSource> CREATOR = new Creator<ImageSource>() {
        @Override
        public ImageSource createFromParcel(Parcel in) {
            return new ImageSource(in);
        }

        @Override
        public ImageSource[] newArray(int size) {
            return new ImageSource[size];
        }
    };
    private String location;
    private int index;

    public ImageSource(String path, int index) {
        this.location = path;
        this.index = index;
    }

    protected ImageSource(Parcel in) {
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