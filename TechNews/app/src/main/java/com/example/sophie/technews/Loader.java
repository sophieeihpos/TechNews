package com.example.sophie.technews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Sophie on 2/28/2017.
 */

public class Loader extends AsyncTaskLoader {
    private String url;
    public Loader(Context context,String url) {
        super(context);
        this.url=url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("Loader","onStartLoading");
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        Log.i("Loader","loadInBackground");
        if (url!=null){
           return Utils.extractNews(url);
        }else{return null;}
    }
}
