package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bat extends GameObject {
    public Bat(Vector2 position, float movementSpeed, Texture gameObjectTexture, float width, float height) {
        super(position, movementSpeed, gameObjectTexture, width, height);
    }

    public void commandMoveLeft() {
        bounds.x -= movementSpeed * Gdx.graphics.getDeltaTime();
        if (bounds.x < 0) bounds.x = 0;
    }

    public void commandMoveRight() {
        bounds.x += movementSpeed * Gdx.graphics.getDeltaTime();
        if (bounds.x > Gdx.graphics.getWidth() - gameObjectTexture.getWidth())
            bounds.x = Gdx.graphics.getWidth() - gameObjectTexture.getWidth();
    }


}
