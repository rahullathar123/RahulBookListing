package com.example.rahul.rahulbooklistingudacity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    Button findButton;

    @BindView(R.id.progress)
    ProgressBar loader;
    @BindView(R.id.search)
    EditText textEntered;
    @BindView(R.id.no_book_found)
    TextView notFound;
    BookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
       findButton = (Button) findViewById(R.id.seach_button);


        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = getCurrentFocus();   //for hiding the keyboard when search button is clicked
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                adapter.clear();

                if (textEntered.getText().toString().trim().matches("")) { //if search box is empty and button is clicked
                    notFound.setVisibility(View.VISIBLE);
                    notFound.setText("Please Enter Something ");
                } else {
                    if (isNetworkConnected()) {
                        //if search box is NOT empty and network is connected
                        BookAsyncTask task = new BookAsyncTask();
                        task.execute();
                    } else {
                        ////if search box is NOT empty and network is disconnected///
                        notFound.setVisibility(View.VISIBLE);
                        notFound.setText("no connection ");
                    }
                }
            }
        });

        ListView bookList = (ListView) findViewById(R.id.list);
        adapter = new BookAdapter(this);
        bookList.setAdapter(adapter);


    }

    public boolean isNetworkConnected() {
        //check network connection
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            // we gonna throw null point exception to see if its connected to the internet or not
            return true;
        else
            return false;
    }

    //get the text which the user entered in the search bar
    public String getText() {
        return textEntered.getText().toString();
    }


    private class BookAsyncTask extends AsyncTask<Void, Void, List<Book>> {

            @Override
            protected void onPreExecute() {
                notFound.setVisibility(View.INVISIBLE);
                // display the progress bar
                loader.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Book> doInBackground(Void... voids) {
                List<Book> result = Utils.fetchData(getText());
                return result;

            }

            @Override
            protected void onPostExecute(List<Book> books) {
                loader.setVisibility(View.INVISIBLE);
                //hide the progress bar

                if (books == null) {
                    notFound.setVisibility(View.VISIBLE);
                    notFound.setText("Didnt find any book");
                } else {
                    notFound.setVisibility(View.GONE);
                    adapter.addAll(books);
                }
            }
    }
}

