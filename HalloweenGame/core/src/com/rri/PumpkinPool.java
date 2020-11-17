package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class PumpkinPool extends Pool<Pumpkin> {
    Texture pumpkinTexture = new Texture("pumpkin.png");

    @Override
    protected Pumpkin newObject() {
        return new Pumpkin(new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()), 400, pumpkinTexture, pumpkinTexture.getWidth(), pumpkinTexture.getHeight());
    }
}
