package com.bojankopanja.humanityui.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by bojankopanja on 8/19/15.
 * Model for AA FlickrImages table
 */
@Table(name="FlickrImages")
public class FlickrImageDb extends Model {

    @Column(name="Path")
    public String path;

    @Column(name="Title")
    public String title;

    @Column(name="Url")
    public String url;

    public FlickrImageDb() {}

    public FlickrImageDb(String path, String title, String url) {
        this.path = path;
        this.title = title;
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
