package com.example.sophie.technews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Sophie on 2/28/2017.
 */

public class NewsArrayAdapter extends ArrayAdapter {
    ArrayList<News> arrayList;
    public NewsArrayAdapter(Context context, ArrayList<News> arrayList) {
        super(context, 0);
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterViewHolder adapterViewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);

            adapterViewHolder = new AdapterViewHolder(convertView);
            convertView.setTag(adapterViewHolder);
        } else {
            adapterViewHolder = (AdapterViewHolder) convertView.getTag();
        }
        News news=(News) getItem(position);
        adapterViewHolder.title.setText(news.getTitle());
        adapterViewHolder.author.setText(news.getAuthor());
        adapterViewHolder.date.setText(news.getDate().replace("T"," ").replace("Z",""));
        adapterViewHolder.section.setText(news.getSection());
        return convertView;
    }

}

