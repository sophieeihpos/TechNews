package com.example.sophie.technews;

import java.util.Date;

/**
 * Created by Sophie on 2/27/2017.
 */

public class News {
    String title;
    String author;
    String date;
    String section;
    String url;
    public News(String title, String author, String date,String section,String url){
        this.title=title;
        this.author=author;
        this.date=date;
        this.section=section;
        this.url=url;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getDate() {
        return date;
    }
    public String getSection(){
        return section;
    }
    public String getUrl(){return url;}
}
