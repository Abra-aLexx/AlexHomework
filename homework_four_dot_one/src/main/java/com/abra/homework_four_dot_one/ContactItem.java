package com.abra.homework_four_dot_one;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactItem implements Parcelable {
    private final int iconId;
    private final String name;
    private final String info;
    private final String typeInfo;

    public ContactItem(int iconId, String name, String info, String typeInfo) {
        this.iconId = iconId;
        this.name = name;
        this.info = info;
        this.typeInfo = typeInfo;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel parcel) {
            return new ContactItem(parcel);
        }

        @Override
        public ContactItem[] newArray(int i) {
            return new ContactItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(iconId);
        parcel.writeString(name);
        parcel.writeString(info);
        parcel.writeString(typeInfo);
    }

    private ContactItem(Parcel parcel) {
        iconId = parcel.readInt();
        name = parcel.readString();
        info = parcel.readString();
        typeInfo = parcel.readString();
    }
}
