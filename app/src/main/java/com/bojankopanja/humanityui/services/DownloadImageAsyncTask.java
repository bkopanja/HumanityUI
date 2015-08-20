package com.bojankopanja.humanityui.services;

import android.os.AsyncTask;

import com.bojankopanja.humanityui.models.FlickrImageApi;
import com.bojankopanja.humanityui.models.FlickrImageDb;
import com.bojankopanja.humanityui.util.Constants;
import com.bojankopanja.humanityui.util.Helper;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bojankopanja on 8/18/15.
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Integer, String> {

    OkHttpClient client;
    DeferredObject deferredObject;
    Promise promise;

    public DownloadImageAsyncTask() {
        client = new OkHttpClient();
        deferredObject = new DeferredObject();
        promise = deferredObject.promise();
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream input = null;
        OutputStream output = null;
        Gson gson = new Gson();
        String imageUrl = "";

            final FlickrImageApi flickrImage = gson.fromJson(params[0], FlickrImageApi.class);

            imageUrl = Helper.getInstance().getFlickrFullImageUrl(
                    flickrImage.getFarm(),
                    flickrImage.getServer(),
                    flickrImage.getId(),
                    flickrImage.getSecret(),
                    "_z");

            String imageSdCardPath = params[1] + "/" + flickrImage.getId() + ".jpg";

            File image = new File(params[1], flickrImage.getId() + ".jpg");

            if(!image.exists()) {

                try {
                    URL url = new URL(imageUrl);
                    input = url.openStream();

                    output = new FileOutputStream(image);

                    byte[] buffer = new byte[Constants.BYTE_IMAGE_SIZE];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                        output.write(buffer, 0, bytesRead);
                    }

                    // save image info in DB
                    FlickrImageDb dbImage = new FlickrImageDb(imageSdCardPath, flickrImage.getTitle(), imageUrl);
                    dbImage.save();

                } catch(MalformedURLException e){
                    e.printStackTrace();
                    imageUrl = "";
                } catch(IOException e) {
                    e.printStackTrace();
                    imageUrl = "";
                } finally {
                    if(output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        return imageUrl;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            deferredObject.resolve(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Promise getPromise() {
        return promise;
    }
}
