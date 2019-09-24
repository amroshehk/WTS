package com.firatnet.wst.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    int id;
    String category;
    String created_at;
    String updated_at;

    public Category(int id, String category, String created_at, String updated_at) {
        this.id = id;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.category);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected Category(Parcel in) {
        this.id = in.readInt();
        this.category = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
