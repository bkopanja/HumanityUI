package com.bojankopanja.humanityui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.activeandroid.query.Select;
import com.bojankopanja.humanityui.R;
import com.bojankopanja.humanityui.adapters.ImageDetailsAdapter;
import com.bojankopanja.humanityui.models.FlickrImage;
import com.bojankopanja.humanityui.models.FlickrImageDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that holds view pager with full image info
 */
public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<FlickrImage> imagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final int index = getIntent().getIntExtra("index", 0);
        viewPager = (ViewPager) findViewById(R.id.pager);

        // if we're on SDK version >= 21 we can work with transitions
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
            viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                    if(android.os.Build.VERSION.SDK_INT >= 21) {
                        startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }

        // fetching the list of images from DB and populating the list for adapter
        List<FlickrImageDb> dbList =  new Select().from(FlickrImageDb.class).execute();
        imagesList = new ArrayList<>();

        for(int i = 0; i < dbList.size(); i++) {
            imagesList.add(new FlickrImage(dbList.get(i)));
        }

        ImageDetailsAdapter adapter = new ImageDetailsAdapter(this, imagesList, index);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
    }
}
