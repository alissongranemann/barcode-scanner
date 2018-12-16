package br.ufsc.barcodescanner;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageSource implements Parcelable {

    private String location;

    public ImageSource(String path) {
        this.location = path;
    }

    protected ImageSource(Parcel in) {
        location = in.readString();
    }

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

    public String getImageLocation() {
        return location;
    }

    public void setImageLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
    }
}