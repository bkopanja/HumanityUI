package com.bojankopanja.humanityui.util;

/**
 * Created by bojankopanja on 8/18/15.
 */
public class Helper {

    public static Helper instance;

    public Helper() {}

    public static Helper getInstance() {
        if(instance == null)
            instance = new Helper();

        return instance;
    }

    /**
     * Method that resolves an image url from given parameters
     * more on all parameters: <a href="https://www.flickr.com/services/api/misc.urls.html">Flickr Docs</a>
     * @param farmId ID of Flickr farm where image is stored
     * @param serverId ID of Flickr server where image is stored
     * @param id ID of Flickr image
     * @param secret Secret of Flickr image
     * @param size Size of Flickr image
     * @return Flickr image URL
     */
    public String getFlickrFullImageUrl(String farmId, String serverId, String id, String secret, String size) {

        return String.format(Constants.FLICKR_IMG_URL, farmId, serverId, id, secret, size);
    }


}
