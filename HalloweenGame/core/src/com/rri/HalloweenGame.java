package com.rri;

import java.util.Iterator;

import com.badlogic.gdx.Game;


public class HalloweenGame extends Game {

    GameScreen gameScreen;


    @Override
    public void create() {
        gameScreen = new GameScreen();
        setScreen(gameScreen);

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }

    @Override
    public void dispose() {
        //super.dispose();
        gameScreen.dispose();
    }
}
