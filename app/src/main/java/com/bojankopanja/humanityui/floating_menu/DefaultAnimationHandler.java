package com.bojankopanja.humanityui.floating_menu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by bojankopanja on 8/19/15.
 */
public class DefaultAnimationHandler extends MenuAnimationHandler {
    protected static final int DURATION = 500;
    protected static final int LAG_BETWEEN_ITEMS = 20;
    private boolean animating;

    public DefaultAnimationHandler() {
        this.setAnimating(false);
    }

    public void animateCog(boolean isOpening) {
        RotateAnimation r; // = new RotateAnimation(ROTATE_FROM, ROTATE_TO);

        Float animateFrom = 0.0f;
        Float animateTo;

        if(isOpening)
            animateTo = -360.0f;
        else
            animateTo = 360.0f;


        r = new RotateAnimation(animateFrom, animateTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration((long)DefaultAnimationHandler.DURATION);
        r.setRepeatCount(0);
        this.menu.getIvCog().startAnimation(r);
    }

    public void animateMenuOpening(Point center) {
        super.animateMenuOpening(center);
        this.setAnimating(true);
        ObjectAnimator lastAnimation = null;

        for(int i = 0; i < this.menu.getSubActionItems().size(); ++i) {
            (this.menu.getSubActionItems().get(i)).view.setScaleX(0.0F);
            (this.menu.getSubActionItems().get(i)).view.setScaleY(0.0F);
            (this.menu.getSubActionItems().get(i)).view.setAlpha(0.0F);
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{(float)((this.menu.getSubActionItems().get(i)).x - center.x + (this.menu.getSubActionItems().get(i)).width / 2)});
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{(float)((this.menu.getSubActionItems().get(i)).y - center.y + (this.menu.getSubActionItems().get(i)).height / 2)});
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, new float[]{720.0F});
            PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0F});
            PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0F});
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{1.0F});
            ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder((this.menu.getSubActionItems().get(i)).view, new PropertyValuesHolder[]{pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA});
            animation.setDuration(500L);
            animation.setInterpolator(new OvershootInterpolator(0.9F));
            animation.addListener(new DefaultAnimationHandler.SubActionItemAnimationListener(this.menu.getSubActionItems().get(i), MenuAnimationHandler.ActionType.OPENING));
            if(i == 0) {
                lastAnimation = animation;
            }

            animation.setStartDelay((long)((this.menu.getSubActionItems().size() - i) * 20));
            animation.start();
        }

        if(lastAnimation != null) {
            lastAnimation.addListener(new LastAnimationListener());
        }

    }

    public void animateMenuClosing(Point center) {
        super.animateMenuOpening(center);
        this.setAnimating(true);
        ObjectAnimator lastAnimation = null;

        for(int i = 0; i < this.menu.getSubActionItems().size(); ++i) {
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{(float)(-((this.menu.getSubActionItems().get(i)).x - center.x + (this.menu.getSubActionItems().get(i)).width / 2))});
            PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{(float)(-((this.menu.getSubActionItems().get(i)).y - center.y + (this.menu.getSubActionItems().get(i)).height / 2))});
            PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, new float[]{-720.0F});
            PropertyValuesHolder pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{0.0F});
            PropertyValuesHolder pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{0.0F});
            PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{0.0F});
            ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder((this.menu.getSubActionItems().get(i)).view, new PropertyValuesHolder[]{pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA});
            animation.setDuration(500L);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.addListener(new DefaultAnimationHandler.SubActionItemAnimationListener(this.menu.getSubActionItems().get(i), ActionType.CLOSING));
            if(i == 0) {
                lastAnimation = animation;
            }

            animation.setStartDelay((long)((this.menu.getSubActionItems().size() - i) * 20));
            animation.start();
        }

        if(lastAnimation != null) {
            lastAnimation.addListener(new LastAnimationListener());
        }

    }

    public boolean isAnimating() {
        return this.animating;
    }

    protected void setAnimating(boolean animating) {
        this.animating = animating;
    }

    protected class SubActionItemAnimationListener implements Animator.AnimatorListener {
        private FloatingMenu.Item subActionItem;
        private ActionType actionType;

        public SubActionItemAnimationListener(FloatingMenu.Item subActionItem, ActionType actionType) {
            this.subActionItem = subActionItem;
            this.actionType = actionType;
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            DefaultAnimationHandler.this.restoreSubActionViewAfterAnimation(this.subActionItem, this.actionType);
        }

        public void onAnimationCancel(Animator animation) {
            DefaultAnimationHandler.this.restoreSubActionViewAfterAnimation(this.subActionItem, this.actionType);
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }
}

