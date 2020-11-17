package com.rri;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class KonceptiTest extends ApplicationAdapter {

    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private Texture texture, texture1;
    private TextureRegion tr;
    static final int WORLD_WIDTH = 200;
    static final int WORLD_HEIGHT = 200;
    float moveSpeed = 100;
    float rotateSpeed = 20;
    float zoomSpeed = 1;

    @Override
    public void create() {
        camera = new OrthographicCamera(WORLD_WIDTH * 5, WORLD_HEIGHT * 2);
        camera.position.set(WORLD_WIDTH * 5 / 2, WORLD_HEIGHT * 2 / 2, 0);
        //viewport = new FitViewport(WORLD_WIDTH * 5, WORLD_HEIGHT * 2, camera);
        viewport = new StretchViewport(WORLD_WIDTH * 5, WORLD_HEIGHT * 2, camera);
        //viewport = new ExtendViewport(WORLD_WIDTH * 5, WORLD_HEIGHT * 2, camera);
        //viewport = new ScreenViewport();
        camera.update();

        texture = new Texture("pumpkin.png");
        texture1 = new Texture("1.png");
        spriteBatch = new SpriteBatch();

    }

    @Override
    public void render() {

        // move camera left and right
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, moveSpeed * Gdx.graphics.getDeltaTime());
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -moveSpeed * Gdx.graphics.getDeltaTime());
        }

        // move camera up and down
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(moveSpeed * Gdx.graphics.getDeltaTime(), 0);
        }

        // zoom camera
        if (Gdx.input.isKeyPressed((Input.Keys.UP))) {
            camera.zoom -= zoomSpeed * Gdx.graphics.getDeltaTime();
        } else if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
            camera.zoom += zoomSpeed * Gdx.graphics.getDeltaTime();
        }

        // rotate camera
        if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) {
            camera.rotate(-rotateSpeed * Gdx.graphics.getDeltaTime());
        } else if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            camera.rotate(rotateSpeed * Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        spriteBatch.begin();
        spriteBatch.draw(texture1, 0, 0, WORLD_WIDTH * 5, WORLD_HEIGHT * 2);
        spriteBatch.draw(texture, WORLD_WIDTH * 5 / 2, WORLD_HEIGHT);
        spriteBatch.draw(texture, 0, 0, 50, 50, 100, 100, 1, 1, 90, 100, 100, 50, 50, false, false);

        spriteBatch.end();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {

        spriteBatch.dispose();
        texture.dispose();
        texture1.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // coordinate system specified by the camera.
        spriteBatch.setProjectionMatrix(camera.combined);
    }
}