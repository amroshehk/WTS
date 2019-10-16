package com.firatnet.wst.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private int id;

    private String category_id;
    private String content;
    private String pic_url;

    private String created_at;
    private String updated_at;

    public News(int id, String category_id, String content, String pic_url, String created_at, String updated_at) {
        this.id = id;
        this.category_id = category_id;
        this.content = content;
        this.pic_url = pic_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
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
        dest.writeString(this.category_id);
        dest.writeString(this.content);
        dest.writeString(this.pic_url);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
    }

    protected News(Parcel in) {
        this.id = in.readInt();
        this.category_id = in.readString();
        this.content = in.readString();
        this.pic_url = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
