package com.bojankopanja.humanityui.models;

/**
 * Created by bojankopanja on 8/19/15.
 * Class for manipulating FlickrImages and using them in adapters
 */
public class FlickrImage implements Comparable<FlickrImage> {

    private long id;
    private String path;
    private String title;
    private String url;

    public FlickrImage() {
    }

    public FlickrImage(FlickrImageDb dbImage) {
        this.id = dbImage.getId();
        this.path = dbImage.getPath();
        this.title = dbImage.getTitle();
        this.url = dbImage.getUrl();
    }

    public FlickrImage(String path, String title, String url) {
        this.id = -1;
        this.path = path;
        this.title = title;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public int compareTo(FlickrImage flickrImage) {
        if(this.getPath().equals(flickrImage.getPath()))
            return 0;

        return 1;
    }
}
