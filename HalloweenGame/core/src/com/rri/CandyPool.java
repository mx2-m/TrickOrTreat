package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class CandyPool extends Pool<Candy> {

    @Override
    protected Candy newObject() {
        Texture candyTexture = new Texture("candy3.png");
        return new Candy(new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 200, candyTexture, candyTexture.getWidth(), candyTexture.getHeight());
    }
}
