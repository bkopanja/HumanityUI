package com.bojankopanja.humanityui.floating_menu;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;

import com.bojankopanja.humanityui.R;

/**
 * Created by bojankopanja on 8/19/15.
 */
public class SubActionButton extends FrameLayout {

    public SubActionButton(Activity activity, LayoutParams layoutParams, int theme, Drawable backgroundDrawable, View contentView, LayoutParams contentParams) {
        super(activity);
        this.setLayoutParams(layoutParams);
        backgroundDrawable = null;

        this.setBackgroundResource(backgroundDrawable);
        if(contentView != null) {
            this.setContentView(contentView, contentParams);
        }

        this.setClickable(true);
    }

    public void setContentView(View contentView, LayoutParams params) {
        if(params == null) {
            params = new LayoutParams(-2, -2, 17);
            int margin = this.getResources().getDimensionPixelSize(R.dimen.sub_action_button_content_margin);
            params.setMargins(margin, margin, margin, margin);
        }

        contentView.setClickable(false);
        this.addView(contentView, params);
    }

    public void setContentView(View contentView) {
        this.setContentView(contentView, (LayoutParams)null);
    }

    private void setBackgroundResource(Drawable drawable) {
        if(Build.VERSION.SDK_INT >= 16) {
            this.setBackground(drawable);
        } else {
            this.setBackgroundDrawable(drawable);
        }

    }

    public static class Builder {
        private Activity activity;
        private LayoutParams layoutParams;
        private int theme;
        private Drawable backgroundDrawable;
        private View contentView;
        private LayoutParams contentParams;

        public Builder(Activity activity) {
            this.activity = activity;
            int size = activity.getResources().getDimensionPixelSize(R.dimen.sub_item_size);
            LayoutParams params = new LayoutParams(size, size, 51);
            this.setLayoutParams(params);
            this.setTheme(0);
        }

        public SubActionButton.Builder setLayoutParams(LayoutParams params) {
            this.layoutParams = params;
            return this;
        }

        public SubActionButton.Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public SubActionButton.Builder setBackgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public SubActionButton.Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public SubActionButton.Builder setContentView(View contentView, LayoutParams contentParams) {
            this.contentView = contentView;
            this.contentParams = contentParams;
            return this;
        }

        public SubActionButton build() {
            return new SubActionButton(this.activity, this.layoutParams, this.theme, this.backgroundDrawable, this.contentView, this.contentParams);
        }
    }
}
