package com.hello_world.vikas.connect_to_iiitd;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String url =  "https://www.iiitd.ac.in/about" ;
    String result1;
    ProgressDialog var;
    private static final String DEBUG_TAG = "HttpExample";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String message = savedInstanceState.getString("message");
            TextView text = (TextView)findViewById(R.id.edittext);
            result1=message;
            text.setText(message);
        }

    }
    // When user clicks button, calls AsyncTask.
    // Before attempting to fetch the URL, makes sure that there
    //is a network connection.
    public void myClickHandler(View view) {
    // Gets the URL from the UI's text field.
        var=new ProgressDialog(this);
        var.show();
        String stringUrl = url;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            Toast.makeText(this, "Network_Connection Error", Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            TextView text = (TextView)findViewById(R.id.edittext);
            result1=result;
            text.setText(result);
            var.dismiss();
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 10000;
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(150000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();
            // Convert the InputStream into a string
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String currentLine = reader.readLine();
            String st="";
            while(currentLine!=null)
            {
                st+=currentLine;
                currentLine=reader.readLine();

            }
            return st;
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("message", result1);
        super.onSaveInstanceState(outState);
    }


}
