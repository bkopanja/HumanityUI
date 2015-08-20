package com.bojankopanja.humanityui.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.activeandroid.query.Select;
import com.bojankopanja.humanityui.R;
import com.bojankopanja.humanityui.adapters.ImagesGridAdapter;
import com.bojankopanja.humanityui.models.FlickrImage;
import com.bojankopanja.humanityui.models.FlickrImageDb;
import com.bojankopanja.humanityui.services.DownloadImageAsyncTask;
import com.bojankopanja.humanityui.services.GetImagesAsyncTask;
import com.bojankopanja.humanityui.util.Constants;

import org.jdeferred.DeferredManager;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DefaultDeferredManager;
import org.jdeferred.multiple.MultipleResults;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity that holds grid view with small images
 */
public class MainActivity extends AppCompatActivity {

    private GridView gwImages;
    private ImagesGridAdapter imagesAdapter;

    private String sdCardRootFolder;
    private List<FlickrImage> adapterImagesList;

    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (LinearLayout) findViewById(R.id.llProgressBar);

        gwImages = (GridView) findViewById(R.id.gwImages);
        adapterImagesList = new ArrayList<>();

        // setup SD card folder
        setupSdCard();

        // load Images info from DB (if any)
        loadImageInfoFromDB();

        // setup grid view with images
        setupGridView();

        // let's fetch more images from Flickr
        GetImagesAsyncTask getImagesAsyncTask = new GetImagesAsyncTask();
        Promise getImagesAsyncTaskPromise = getImagesAsyncTask.getPromise();
        getImagesAsyncTask.execute("puppies,cats,nature", "1", Constants.loadImages.toString());

        getImagesAsyncTaskPromise.done(new DoneCallback() {
            @Override
            public void onDone(Object result) {

                try {
                    final JSONObject images = new JSONObject(result.toString());
                    Log.d(Constants.TAG, result.toString());

                    DeferredManager deferredManager = new DefaultDeferredManager();
                    Promise[] promiseArray = new Promise[Constants.loadImages];

                    JSONArray jaPhotos = images.getJSONObject("photos").getJSONArray("photo");

                    for (int i = 0; i < jaPhotos.length(); i++) {

                        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask();
                        promiseArray[i] = downloadImageAsyncTask.getPromise();
                        downloadImageAsyncTask.execute(jaPhotos.getString(i), sdCardRootFolder);

                    }

                    deferredManager.when(promiseArray).done(new DoneCallback<MultipleResults>() {
                        @Override
                        public void onDone(MultipleResults result) {
                            loadImageInfoFromDB();
                            imagesAdapter.notifyDataSetChanged();
                            Log.d(Constants.TAG, "Fetched and saved new images");
                        }
                    });

                } catch (Exception exception) {
                    Log.e(Constants.TAG, exception.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * Method that checks if SD Card contains the folder for
     * storing the images and creates it if it doesn't already exist
     */
    public void setupSdCard() {

        String root = Environment.getExternalStorageDirectory().toString();
        sdCardRootFolder = root + Constants.SD_CARD_FOLDER;
        File myDir = new File(sdCardRootFolder);

        if(!myDir.exists())
            myDir.mkdirs();
    }

    /**
     * Fetching images from DB, and populating the list
     * for {@link #gwImages} adapter ({@link #imagesAdapter})
     */
    public void loadImageInfoFromDB() {

        List<FlickrImageDb> imageDbList = new Select().from(FlickrImageDb.class).execute();

        for(int i = 0; i < imageDbList.size(); i++) {
            FlickrImageDb dbImage = imageDbList.get(i);
            FlickrImage image = new FlickrImage(dbImage);

            Boolean contains = false;
            for(FlickrImage arrImage : adapterImagesList) {
                if(arrImage.getPath().equals(image.getPath())) {
                    contains = true;
                    break;
                }
            }

            if(!contains)
                adapterImagesList.add(image);
        }

        if(adapterImagesList.size() > 0 && progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Setting up the {@link #imagesAdapter} and {@link #gwImages}
     */
    public void setupGridView() {
        if(adapterImagesList == null)
            adapterImagesList = new ArrayList<>();

        imagesAdapter = new ImagesGridAdapter(this, adapterImagesList);
        gwImages.setAdapter(imagesAdapter);
    }
}
