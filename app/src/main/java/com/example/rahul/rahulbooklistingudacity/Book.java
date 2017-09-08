package com.example.rahul.rahulbooklistingudacity;

/**
 * Created by rahul on 2017-08-26.
 */


public class Book {

    private String mAuthors;
    private String mTitle;

    public Book(String authors_name, String bookName) {
        mAuthors = authors_name;
        mTitle = bookName;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getTitles() {
        return mTitle;
    }
}
