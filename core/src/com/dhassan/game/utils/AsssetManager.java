package com.dhassan.game.utils;

import com.badlogic.gdx.assets.AssetManager;

public class AsssetManager {


    private static AsssetManager singleObject;

    private final AssetManager manager;


    private AsssetManager() {
        manager = new AssetManager();
    }

    public static AssetManager get() {

        // create object if it's not already created
        if (singleObject == null) {
            singleObject = new AsssetManager();
        }

        // returns the singleton object
        return singleObject.manager;
    }

}
