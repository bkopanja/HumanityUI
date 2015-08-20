package com.bojankopanja.humanityui.util;

/**
 * Created by bojankopanja on 8/18/15.
 */
public class Constants {

    private Constants() {}

    public static String TAG = "HumanityUI app";

    // Flickr API Key
    public static String API_KEY = "8a98a99040f30dfb8631d79764472ba0";

    // SD Card root folder for images
    public static String SD_CARD_FOLDER = "/saved_images";

    // Max size for image on SD card in bytes
    public static int BYTE_IMAGE_SIZE = 1048576;

    // Flickr image URL template
    // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
    public static String FLICKR_IMG_URL = "https://farm%s.staticflickr.com/%s/%s_%s%s.jpg";

    // How many images to load from Flickr
    public static Integer loadImages = 24;

}
