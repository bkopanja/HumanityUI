package com.bojankopanja.humanityui.models;

/**
 * Created by bojankopanja on 8/19/15.
 * Class that represents Flickr response of image object
 */
public class FlickrImageApi {

    private String id;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private String title;
    private Integer ispublic;
    private Integer isfriend;
    private Integer isfamily;

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }
}
