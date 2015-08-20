package com.bojankopanja.humanityui.floating_menu;

import android.animation.Animator;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by bojankopanja on 8/19/15.
 */
public abstract class MenuAnimationHandler {
    protected FloatingMenu menu;

    public MenuAnimationHandler() {
    }

    public void setMenu(FloatingMenu menu) {
        this.menu = menu;
    }

    public void animateCog(boolean isOpening) {
        if(this.menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingMenu.");
        }
    }

    public void animateMenuOpening(Point center) {
        if(this.menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingMenu.");
        }
    }

    public void animateMenuClosing(Point center) {
        if(this.menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingMenu.");
        }
    }

    protected void restoreSubActionViewAfterAnimation(FloatingMenu.Item subActionItem, MenuAnimationHandler.ActionType actionType) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)subActionItem.view.getLayoutParams();
        subActionItem.view.setTranslationX(0.0F);
        subActionItem.view.setTranslationY(0.0F);
        subActionItem.view.setRotation(0.0F);
        subActionItem.view.setScaleX(1.0F);
        subActionItem.view.setScaleY(1.0F);
        subActionItem.view.setAlpha(1.0F);
        if(actionType == MenuAnimationHandler.ActionType.OPENING) {
            params.setMargins(subActionItem.x, subActionItem.y, 0, 0);
            subActionItem.view.setLayoutParams(params);
        } else if(actionType == MenuAnimationHandler.ActionType.CLOSING) {
            Point center = this.menu.getActionViewCenter();
            params.setMargins(center.x - subActionItem.width / 2, center.y - subActionItem.height / 2, 0, 0);
            subActionItem.view.setLayoutParams(params);
            ((ViewGroup)this.menu.getActivityContentView()).removeView(subActionItem.view);
        }

    }

    public abstract boolean isAnimating();

    protected abstract void setAnimating(boolean var1);

    public class LastAnimationListener implements Animator.AnimatorListener {
        public LastAnimationListener() {
        }

        public void onAnimationStart(Animator animation) {
            MenuAnimationHandler.this.setAnimating(true);
        }

        public void onAnimationEnd(Animator animation) {
            MenuAnimationHandler.this.setAnimating(false);
        }

        public void onAnimationCancel(Animator animation) {
            MenuAnimationHandler.this.setAnimating(false);
        }

        public void onAnimationRepeat(Animator animation) {
            MenuAnimationHandler.this.setAnimating(true);
        }
    }

    protected enum ActionType {
        OPENING,
        CLOSING;

        ActionType() {
        }
    }
}

