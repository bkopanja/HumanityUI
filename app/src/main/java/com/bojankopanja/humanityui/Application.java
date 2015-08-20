package com.bojankopanja.humanityui;

import com.activeandroid.ActiveAndroid;

/**
 * Created by bojankopanja on 8/18/15.
 */
public class Application extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize AA
        ActiveAndroid.initialize(this);
    }
}
