package com.rri;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

abstract class GameObject {
    public final Vector2 position;
    public float movementSpeed;
    public Texture gameObjectTexture;
    public final Rectangle bounds;
    public float width;
    public float height;

    public GameObject(Vector2 position, float movementSpeed, Texture gameObjectTexture, float width, float height) {
        this.position = position;
        this.movementSpeed = movementSpeed;
        this.gameObjectTexture = gameObjectTexture;
        this.bounds = new Rectangle(position.x, position.y, width, height);
    }


    public void draw(Batch batch) {
        batch.draw(gameObjectTexture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

}
