package com.bojankopanja.humanityui.services;

import android.os.AsyncTask;

import com.bojankopanja.humanityui.util.Constants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bojankopanja on 8/18/15.
 */
public class GetImagesAsyncTask extends AsyncTask<String, Integer, JSONObject> {

    OkHttpClient client;
    DeferredObject deferredObject;
    Promise promise;

    public GetImagesAsyncTask() {
        client = new OkHttpClient();
        deferredObject = new DeferredObject();
        promise = deferredObject.promise();
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        String url = String.format("https://api.flickr.com/services/rest/?api_key=%s&method=%s" +
                        "&tags=%s&page=%d&per_page=%d&format=json&nojsoncallback=1&media=photos",
                Constants.API_KEY,
                "flickr.photos.search",
                params[0],
                Integer.parseInt(params[1]),
                Integer.parseInt(params[2]));

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        String responseString;

        try {
            response = client.newCall(request).execute();
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            responseString = "";
        }

        try {
            return new JSONObject(responseString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        deferredObject.resolve(response);
    }

    public Promise getPromise() {
        return promise;
    }
}
