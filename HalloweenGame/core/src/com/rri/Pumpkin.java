package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;


public class Pumpkin extends GameObject implements Pool.Poolable {

    public static long TIME = 0;
    public static float SPEED = 300;
    public static final long CREATE_PUMPKIN_TIME = 2000000000;


    public Pumpkin(Vector2 position, float movementSpeed, Texture gameObjectTexture, float width, float height) {
        super(position, movementSpeed, gameObjectTexture, width, height);
        this.movementSpeed = SPEED;

    }

    @Override
    public void reset() {
        position.set(0, 0);

    }
/*
    public void draw(Batch batch) {
        for (Rectangle pumpkin : pumpkinArray)
            batch.draw(gameObjectTexture, pumpkin.x, pumpkin.y, pumpkin.width, pumpkin.height);

    }*/


}
