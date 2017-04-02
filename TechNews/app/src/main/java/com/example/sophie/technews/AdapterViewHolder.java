package com.example.sophie.technews;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Sophie on 2/28/2017.
 */

public class AdapterViewHolder {
    public TextView title;
    public TextView date;
    public TextView author;
    public TextView section;

    public AdapterViewHolder(View view) {
        this.title = (TextView)view
                .findViewById(R.id.title);
        this.date = (TextView)view
                .findViewById(R.id.date);
        this.author = (TextView)view
                .findViewById(R.id.author);
        this.section=(TextView) view.findViewById(R.id.section);
    }
}
