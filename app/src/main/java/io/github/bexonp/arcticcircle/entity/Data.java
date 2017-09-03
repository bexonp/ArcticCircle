package io.github.bexonp.arcticcircle.entity;

/**
 * Created by Bexon Pak on 2017/08/30.
 */

public class Data {
    private String title;
    private String url;
    private String author;
    private String date;
    private String excerpt;

    public Data(String title, String url, String author, String date, String excerpt) {
        this.title = title;
        this.url = url;
        this.author = author;
        this.date = date;
        this.excerpt = excerpt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }
}
