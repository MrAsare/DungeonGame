package com.dhassan.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.dhassan.DungeonGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setWindowedMode(1080, 720);
        config.setTitle("Dungeon game");
        config.useVsync(true);
        new Lwjgl3Application(new DungeonGame(arg[0]), config);
        }
}
