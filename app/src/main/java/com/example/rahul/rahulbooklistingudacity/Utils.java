package com.example.rahul.rahulbooklistingudacity;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


final class Utils {


    public static String LOG_TAG = Utils.class.getSimpleName();
    static String apiURL = "https://www.googleapis.com/books/v1/volumes?q=search+";

    public static List<Book> fetchData(String enteredText) {

        URL url = createURL(enteredText);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequests(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Book> book = extractFromJson(jsonResponse);
        return book;
    }

    private static URL createURL(String enteredText) {

        URL url = null;
        String urlModify = enteredText.trim().replaceAll("\\s+", "+");
        try {
            url = new URL(apiURL + urlModify);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error during creating modify URL ", e);
        }
        return url; //if fail then null will be returned as default is null
    }

    private static String makeHttpRequests(URL url) throws IOException {
        String jsonResponse = "";
        int timeout1 = 10000;
        int timeout2 = 15000;

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(timeout1); // milliseconds
            urlConnection.setConnectTimeout(timeout2); // milliseconds
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
            // catch the exception thrown by url connection//
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Books JSON results.", e);
            //exectue the statemnt regardless of
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        // / exception will be handled at the calling place(i.e. where call is made()//
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line1 = reader.readLine();
            while (line1 != null) {
                output.append(line1);
                line1 = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> extractFromJson(String jsonBook) {

        List<Book> bookList = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonBook)) {
            return null;
        }

        try {
            //converts JSON response which is in string to JSONObject
            JSONObject root = new JSONObject(jsonBook);
            //this call can throw exception and it would be reported by the java complier so thats why i am not putting throws exception,instead handling it here
            int count = root.getInt("totalItems");
            if (count == 0) {
                //no book found if its zero
                return null;
            }

            JSONArray bookArray = root.getJSONArray("items");

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                String Title = volumeInfo.getString("title");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                String nameAuthors = extractAuthors(authorsArray);


                Book book = new Book(nameAuthors, Title);
                bookList.add(book);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing  JSON results", e);
        }
        return bookList;
    }

    // To extract array of authors and concatenate all author names like "- author1,author2,author3" in a single string
    private static String extractAuthors(JSONArray authorsArray) throws JSONException {

        String authorsName = null;

        if (authorsArray.length() == 0)
            authorsName = "No Author Found";

        for (int i = 0; i < authorsArray.length(); i++) {
            if (i == 0)
                authorsName = "- " + authorsArray.getString(0);
            else
                authorsName = authorsName + ", " + authorsArray.getString(i);
        }

        return authorsName;
    }

}


