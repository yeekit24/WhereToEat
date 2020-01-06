package ploywide.com.wheretoeat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlacesItem implements Parcelable {
    private String placeName,placeAddress;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public PlacesItem(String placeName, String placeAddress) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
    }

    public PlacesItem(){

    }

    private PlacesItem(Parcel in) {
        placeName = in.readString();
        placeAddress = in.readString();
    }

    public static final Creator<PlacesItem> CREATOR = new Creator<PlacesItem>() {
        @Override
        public PlacesItem createFromParcel(Parcel in) {
            return new PlacesItem(in);
        }

        @Override
        public PlacesItem[] newArray(int size) {
            return new PlacesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(placeAddress);
    }
}
