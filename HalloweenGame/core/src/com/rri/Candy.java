package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;


public class Candy extends GameObject implements Pool.Poolable {

    public static long TIME = 0;
    public static final long CREATE_CANDY_TIME = 1000000000;


    public Candy(Vector2 position, float movementSpeed, Texture gameObjectTexture, float width, float height) {
        super(position, movementSpeed, gameObjectTexture, width, height);
    }

    @Override
    public void reset() {
        position.set(0, 0);
        width = 100;
        height = 676;
        //candyTime=2;
    }
}
