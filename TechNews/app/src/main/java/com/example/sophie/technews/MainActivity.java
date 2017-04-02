package com.example.sophie.technews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    private final static String URL="http://content.guardianapis.com/" +
            "search?tag=technology/technology&production-office=uk&order-by=newest&show-tags=contributor&format=json&api-key=test";
    public static final String LOG_TAG = MainActivity.class.getName();
    public NewsArrayAdapter newsArrayAdapter;
    public static final int LOADER_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressBar progressBar=(android.widget.ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);
        ListView listView = (ListView) findViewById(R.id.list_view);
        final TextView emptyView= (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        newsArrayAdapter = new NewsArrayAdapter(this, new ArrayList<News>());
        listView.setAdapter(newsArrayAdapter);

        final LoaderManager loaderManager = getLoaderManager();
        newsArrayAdapter.clear();
        emptyView.setText("");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
            Log.i(LOG_TAG,"initLoader");
        }else {
            emptyView.setText(R.string.no_internet_connection);
            progressBar.setVisibility(View.INVISIBLE);
        }
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                newsArrayAdapter.clear();
                emptyView.setText("");
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    loaderManager.restartLoader(LOADER_ID, null, MainActivity.this);
                    Log.i(LOG_TAG,"restartLoader");
                }else {
                    emptyView.setText(R.string.no_internet_connection);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                News currentBook = (News) newsArrayAdapter.getItem(i);
                String url = currentBook.getUrl();
                if (url == Utils.UNKNOWN_STRING) {
                    Toast.makeText(MainActivity.this, R.string.no_weblink, Toast.LENGTH_SHORT).show();
                } else {
                    intent.setData(Uri.parse(currentBook.getUrl()));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public android.content.Loader<ArrayList<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"onCreateLoader");
        return new Loader(this,URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<News>> loader, ArrayList<News> newsList) {
        Log.i(LOG_TAG,"onLoaderFinished");
        newsArrayAdapter.clear();
        if (newsList != null && !newsList.isEmpty()) {
            newsArrayAdapter.addAll(newsList);
        }
        ProgressBar progressBar=(ProgressBar) findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<News>> loader) {
        Log.i(LOG_TAG,"onLoaderReset");
        newsArrayAdapter.clear();
    }

}
