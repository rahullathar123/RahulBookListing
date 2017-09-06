package com.example.rahul.rahulbooklistingudacity;

/**
 * Created by rahul on 2017-08-26.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context) {
        super(context, -1);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Book book = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }

        TextView title = (TextView) view.findViewById(R.id.titles);
        TextView author = (TextView) view.findViewById(R.id.authors);
        title.setText(book.getTitles());
        author.setText(book.getAuthors());

        return view;
    }
}





