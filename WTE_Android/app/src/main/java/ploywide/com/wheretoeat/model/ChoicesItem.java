package ploywide.com.wheretoeat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChoicesItem implements Parcelable {
    public long server_choice_id;
    private String choices;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    private int drawable;

    public ChoicesItem(){}

    public ChoicesItem(String choices, int drawable, long server_choice_id) {
        this.choices = choices;
        this.drawable = drawable;
        this.server_choice_id = server_choice_id;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices, long server_choice_id) {
        this.choices = choices;
        this.server_choice_id = server_choice_id;
    }

    private ChoicesItem(Parcel in) {
        choices = in.readString();
        drawable = in.readInt();
        server_choice_id = in.readLong();
    }

    public static final Creator<ChoicesItem> CREATOR = new Creator<ChoicesItem>() {
        @Override
        public ChoicesItem createFromParcel(Parcel in) {
            return new ChoicesItem(in);
        }

        @Override
        public ChoicesItem[] newArray(int size) {
            return new ChoicesItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(choices);
        parcel.writeInt(drawable);
        parcel.writeLong(server_choice_id);
    }
}
