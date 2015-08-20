package com.bojankopanja.humanityui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.activeandroid.query.Delete;
import com.bojankopanja.humanityui.R;
import com.bojankopanja.humanityui.activities.DetailsActivity;
import com.bojankopanja.humanityui.floating_menu.FloatingMenu;
import com.bojankopanja.humanityui.floating_menu.SubActionButton;
import com.bojankopanja.humanityui.models.FlickrImage;
import com.bojankopanja.humanityui.models.FlickrImageDb;
import com.jensdriller.libs.undobar.UndoBar;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by bojankopanja on 8/19/15.
 */
public class ImagesGridAdapter extends BaseAdapter implements UndoBar.Listener {

    private Activity activity;
    private List<FlickrImage> data;
    private String deleteFilePath;

    public ImagesGridAdapter(Activity context, List<FlickrImage> data) {
        this.activity = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int index = i;
        final FloatingMenu actionMenu;
        FlickrImage image = data.get(i);
        final ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.images_grid_item, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.ivImage = (ImageView) view.findViewById(R.id.ivImage);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        File imgFile = new File(image.getPath());

        if (imgFile.exists()) {
            Uri uri = Uri.fromFile(imgFile);
            Picasso.with(activity).load(uri).into(viewHolder.ivImage);
        }

        actionMenu = new FloatingMenu.Builder(activity)
                .attachTo(view)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(activity);

        // Creating floating menu item for "delete image" action
        ImageView itemIcon = new ImageView(activity);
        itemIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ico_delete));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                deleteImage(index);
            }
        });

        // Creating floating menu item for "share image" action
        ImageView itemIcon2 = new ImageView(activity);
        itemIcon2.setImageDrawable(activity.getResources().getDrawable(R.drawable.ico_share));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.close(true);
                shareImage(index);
            }
        });

        actionMenu.getSubActionItems().add(new FloatingMenu.Item(button1, 128, 128));
        actionMenu.getSubActionItems().add(new FloatingMenu.Item(button2, 128, 128));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(activity, DetailsActivity.class);
                detailsIntent.putExtra("index", index);

                if(actionMenu.isOpen())
                    actionMenu.close(true);

                Pair<View, String> pair = Pair.create((View)viewHolder.ivImage, "image");

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);
                activity.startActivity(detailsIntent, optionsCompat.toBundle());
            }
        });

        return view;
    }

    /**
     * Method that invokes intent for sharing data
     * @param position int position of clicked item from GridView
     */
    private void shareImage(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data.get(position).getUrl());
        sendIntent.setType("text/plain");
        activity.startActivity(sendIntent);
    }

    /**
     * Method that invokes del process of selected image from SD card and GridView
     * @param position int position of clicked item from GridView
     */
    private void deleteImage(int position) {

        FlickrImage image = data.get(position);

        final Bundle imageUndoToken = new Bundle();
        imageUndoToken.putString("path", image.getPath());
        imageUndoToken.putString("url", image.getUrl());
        imageUndoToken.putString("title", image.getTitle());

        this.deleteFilePath = image.getPath();

        new Delete().from(FlickrImageDb.class).where("Id = ?", image.getId()).execute();

        data.remove(position);
        this.notifyDataSetChanged();

        new UndoBar.Builder(activity)
                .setMessage("Image deleted successfully")
                .setListener(this)
                .setUndoToken(imageUndoToken)
                .show();
    }

    @Override
    public void onHide() {
        if(this.deleteFilePath != null) {
            File file = new File(this.deleteFilePath);
            file.delete();
            this.deleteFilePath = null;
        }
    }

    @Override
    public void onUndo(Parcelable parcelable) {
        if(parcelable != null) {
            Bundle imageBundle = (Bundle) parcelable;

            FlickrImageDb dbImage = new FlickrImageDb(imageBundle.getString("path"),
                    imageBundle.getString("title"),
                    imageBundle.getString("url"));
            dbImage.save();

            this.deleteFilePath = null;

            data.add(new FlickrImage(dbImage));
            notifyDataSetChanged();
        }
    }

    /**
     * View holder class for GridView item
     */
    static class ViewHolder {
        ImageView ivImage;
    }
}
