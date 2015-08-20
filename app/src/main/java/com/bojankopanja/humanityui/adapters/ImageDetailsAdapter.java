package com.bojankopanja.humanityui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bojankopanja.humanityui.R;
import com.bojankopanja.humanityui.models.FlickrImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by bojankopanja on 8/19/15.
 */
public class ImageDetailsAdapter extends PagerAdapter {

    private Context context;
    private List<FlickrImage> data;
    private LayoutInflater layoutInflater;
    private int index;


    /**
     * Adapter for ViewPager with image details (user in {@link com.bojankopanja.humanityui.activities.DetailsActivity})
     * @param context Context that we pass from the activity {@link com.bojankopanja.humanityui.activities.DetailsActivity}
     * @param data List of {@link FlickrImage} objects
     * @param index Current active index so we know which imageView needs to have transition name
     */
    public ImageDetailsAdapter(Context context, List<FlickrImage> data, int index) {
        this.context = context;
        this.data = data;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.index = index;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        FlickrImage image = data.get(position);
        final ViewHolder viewHolder;

        View itemView = layoutInflater.inflate(R.layout.view_pager_item, container, false);

        // There's no use working with ViewHolder here as this won't be ever recycled
        // but I prefer having a grip of views inside an object so I'll use it anyway :)
        viewHolder = new ViewHolder();
        viewHolder.ivImage = (ImageView) itemView.findViewById(R.id.ivDetailsImage);
        viewHolder.tvTitle = (TextView) itemView.findViewById(R.id.tvImageTitle);

        File imgFile = new File(image.getPath());

        if (imgFile.exists()) {

            if(index == position && android.os.Build.VERSION.SDK_INT >= 21)
                viewHolder.ivImage.setTransitionName("image");

            viewHolder.tvTitle.setText(image.getTitle());
            viewHolder.tvTitle.setMaxLines(2);

            Uri uri = Uri.fromFile(imgFile);
            final View finalItemView = itemView;

            Picasso.with(context).load(uri).into(viewHolder.ivImage, new Callback() {
                @Override
                public void onSuccess() {
                    detectPalette(viewHolder.ivImage, finalItemView);
                }

                @Override
                public void onError() {

                }
            });
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    /**
     * Method that creates a color palette of an image
     * @param imageView image that we're taking the palette of
     * @param parent view that's parent to "palette color holder" views
     */
    public void detectPalette(ImageView imageView, View parent) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
        if(bitmapDrawable!= null) {
            Bitmap bmp = bitmapDrawable.getBitmap();
            if (bmp != null) {
                Palette palette = Palette.from(bmp).generate();

                View vColor1 = parent.findViewById(R.id.viewColor1);
                View vColor2 = parent.findViewById(R.id.viewColor2);
                View vColor3 = parent.findViewById(R.id.viewColor3);
                View vColor4 = parent.findViewById(R.id.viewColor4);
                View vColor5 = parent.findViewById(R.id.viewColor5);
                View vColor6 = parent.findViewById(R.id.viewColor6);

                getColorFromImageToView(1, palette, vColor1);
                getColorFromImageToView(2, palette, vColor2);
                getColorFromImageToView(3, palette, vColor3);
                getColorFromImageToView(4, palette, vColor4);
                getColorFromImageToView(5, palette, vColor5);
                getColorFromImageToView(6, palette, vColor6);
            }
        }
    }

    /**
     * Method that returns the color of color from the palette on certain position
     * @param colorPosition position of color we're retrieving (int value 1-6)
     * @param palette palette which has all the colors
     * @param v view that will represent the color from the palette
     */
    public void getColorFromImageToView(int colorPosition, Palette palette, View v) {

        int color = 0;

        switch (colorPosition) {
            case 1:
                color = palette.getVibrantColor(0x000000);
                break;
            case 2:
                color = palette.getLightVibrantColor(0x000000);
                break;
            case 3:
                color = palette.getDarkVibrantColor(0x000000);
                break;
            case 4:
                color = palette.getMutedColor(0x000000);
                break;
            case 5:
                color = palette.getLightMutedColor(0x000000);
                break;
            case 6:
                color = palette.getDarkMutedColor(0x000000);
                break;
        }

        v.setBackgroundColor(color);
        if(color == 0)
            v.setVisibility(View.GONE);
        else
            v.setVisibility(View.VISIBLE);
    }

    /**
     * View holder class for detailed image view
     */
    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
    }
}
