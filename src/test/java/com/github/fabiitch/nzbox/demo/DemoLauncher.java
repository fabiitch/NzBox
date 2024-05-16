package com.github.fabiitch.nzbox.demo;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DemoLauncher {
    private static final int WITDH = 800;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("BoxDemo");
        configuration.setWindowedMode(WITDH, HEIGHT);
        configuration.setWindowIcon("box.png");


        ApplicationListener applicationListener = new Game() {
            @Override
            public void create() {
                DemoAllShape demoScren = new DemoAllShape();
                setScreen(demoScren);
            }
        };
        new Lwjgl3Application(applicationListener, configuration);
    }
}
