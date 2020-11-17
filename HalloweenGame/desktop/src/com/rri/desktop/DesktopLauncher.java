package com.rri.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.rri.HalloweenGame;
import com.rri.HalloweenGame00;
import com.rri.KonceptiTest;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "HalloweenGame";
        config.width = 1000;
        config.height = 400;
        config.forceExit = false;
        config.vSyncEnabled = false; // Setting to false disables vertical sync
        config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling ACTIVE APP
        config.backgroundFPS = 0; // Setting to 0 disables background fps throttling INACTIVE APP


        //new LwjglApplication(new KonceptiTest(), config);
        new LwjglApplication(new HalloweenGame(), config);
    }
}
