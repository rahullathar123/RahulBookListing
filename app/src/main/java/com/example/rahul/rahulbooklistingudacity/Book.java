package com.example.rahul.rahulbooklistingudacity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rahul on 2017-08-26.
 */


public class Book implements Parcelable {

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String mAuthors;
    private String mTitle;

    public Book(String authors_name, String bookName) {
        mAuthors = authors_name;
        mTitle = bookName;
    }

    protected Book(Parcel in) {
        this.mAuthors = in.readString();
        this.mTitle = in.readString();
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getTitles() {
        return mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAuthors);
        dest.writeString(this.mTitle);
    }
}