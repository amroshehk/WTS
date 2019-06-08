package com.firatnet.wts.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String created_date;
    private String updated_date;

    public Post(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Post(int id, String title, String description, String created_date, String updated_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }

    public String getCreated_date() {

        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public Post() {
    }

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.created_date);
        dest.writeString(this.updated_date);
    }

    protected Post(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.created_date = in.readString();
        this.updated_date = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
