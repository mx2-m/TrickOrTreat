package com.rri;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

// Vaja1

public class HalloweenGame00 extends ApplicationAdapter {
    private Texture candyImage;
    private Texture batImage;
    private Texture pumpkinImage;
    private Sound sound;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Rectangle bat;
    private Array<Rectangle> candy; //special LibGDX Array
    private Array<Rectangle> pumpkin;
    private long lastCandyTime;
    private long lastPumpkinTime;
    private int candyScore;
    private int batHealth; //Starts with 100

    private BitmapFont font;

    //Values are set experimental
    private static int SPEED = 600; // pixels per second
    private static int SPEED_CANDY = 200; // pixels per second
    private static int SPEED_PUMPKIN = 100; // pixels per second
    private static long CREATE_CANDY_TIME = 1000000000; //ns
    private static long CREATE_PUMPKIN_TIME = 2000000000; //ns

    private void commandMoveLeft() {
        bat.x -= SPEED * Gdx.graphics.getDeltaTime();
        if (bat.x < 0) bat.x = 0;
    }

    private void commandMoveReght() {
        bat.x += SPEED * Gdx.graphics.getDeltaTime();
        if (bat.x > Gdx.graphics.getWidth() - batImage.getWidth())
            bat.x = Gdx.graphics.getWidth() - batImage.getWidth();
    }

    private void commandMoveLeftCorner() {
        bat.x = 0;
    }

    private void commandMoveRightCorner() {
        bat.x = Gdx.graphics.getWidth() - batImage.getWidth();
    }

    private void commandTouched() {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPos);
        bat.x = touchPos.x - batImage.getWidth() / 2;
    }

    private void commandExitGame() {
        Gdx.app.exit();
    }

    @Override
    public void create() {

        font = new BitmapFont();
        font.getData().setScale(2);
        candyScore = 0;
        batHealth = 100;

        // default way to load texture
        batImage = new Texture(Gdx.files.internal("bat1.png"));
        candyImage = new Texture(Gdx.files.internal("candy3.png"));
        pumpkinImage = new Texture(Gdx.files.internal("pumpkin.png"));
        sound = Gdx.audio.newSound(Gdx.files.internal("pick.wav"));    //PROMJENITI

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        // create a Rectangle to logically represents the rocket
        bat = new Rectangle();
        bat.x = Gdx.graphics.getWidth() / 2 - batImage.getWidth() / 2; // center the rocket horizontally
        bat.y = 20; // bottom left corner of the rocket is 20 pixels above the bottom screen edge
        bat.width = batImage.getWidth();
        bat.height = batImage.getHeight();

        candy = new Array<Rectangle>();
        pumpkin = new Array<Rectangle>();
        //add first astronoutn and asteroid
        spawnCandy();
        spawnPumpkin();

    }

    private void spawnCandy() {
        Rectangle candy = new Rectangle();
        candy.x = MathUtils.random(0, Gdx.graphics.getWidth() - candyImage.getWidth());
        candy.y = Gdx.graphics.getHeight();
        candy.width = candyImage.getWidth();
        candy.height = candyImage.getHeight();
        this.candy.add(candy);
        lastCandyTime = TimeUtils.nanoTime();
    }

    private void spawnPumpkin() {
        Rectangle pumpkin = new Rectangle();
        pumpkin.x = MathUtils.random(0, Gdx.graphics.getWidth() - candyImage.getWidth());
        pumpkin.y = Gdx.graphics.getHeight();
        pumpkin.width = pumpkinImage.getWidth();
        pumpkin.height = pumpkinImage.getHeight();
        this.pumpkin.add(pumpkin);
        lastPumpkinTime = TimeUtils.nanoTime();
    }


    @Override
    public void render() { //runs every frame
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the rocket, astronauts, asteroids
        batch.begin();
        { //add brackets just for intent
            batch.draw(batImage, bat.x, bat.y);
            for (Rectangle pumpkin : pumpkin) {
                batch.draw(pumpkinImage, pumpkin.x, pumpkin.y);

            }
            for (Rectangle candy : candy) {
                batch.draw(candyImage, candy.x, candy.y);
            }
            font.setColor(Color.ORANGE);
            font.draw(batch, "" + candyScore, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 20);
            font.setColor(Color.RED);
            font.draw(batch, "" + batHealth, 20, Gdx.graphics.getHeight() - 20);
        }
        batch.end();

        // process user input
        if (Gdx.input.isTouched()) commandTouched(); //mouse or touch screen
        if (Gdx.input.isKeyPressed(Keys.LEFT)) commandMoveLeft();
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) commandMoveReght();
        if (Gdx.input.isKeyPressed(Keys.A)) commandMoveLeftCorner();
        if (Gdx.input.isKeyPressed(Keys.S)) commandMoveRightCorner();
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) commandExitGame();

        // check if we need to create a new
        if (TimeUtils.nanoTime() - lastCandyTime > CREATE_CANDY_TIME) spawnCandy();
        if (TimeUtils.nanoTime() - lastPumpkinTime > CREATE_PUMPKIN_TIME) spawnPumpkin();

        if (batHealth > 0) { //is game end?
            // move and remove any that are beneath the bottom edge of
            // the screen or that hit the rocket.
            for (Iterator<Rectangle> iter = pumpkin.iterator(); iter.hasNext(); ) {
                Rectangle pumpkin = iter.next();
                pumpkin.y -= SPEED_PUMPKIN * Gdx.graphics.getDeltaTime();
                if (pumpkin.y + pumpkinImage.getHeight() < 0) iter.remove();
                if (pumpkin.overlaps(bat)) {
                    sound.play();
                    batHealth--;
                }
            }

            for (Iterator<Rectangle> iter = candy.iterator(); iter.hasNext(); ) {
                Rectangle candy = iter.next();
                candy.y -= SPEED_CANDY * Gdx.graphics.getDeltaTime();
                if (candy.y + candyImage.getHeight() < 0) iter.remove(); //From screen
                if (candy.overlaps(bat)) {
                    sound.play();
                    candyScore++;
                    if (candyScore % 10 == 0) SPEED_PUMPKIN += 66; //speeds up
                    iter.remove(); //smart Array enables remove from Array
                }
            }
        } else { //health of rocket is 0 or less
            batch.begin();
            {
                font.setColor(Color.RED);
                font.draw(batch, "GAME OVER", Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight() / 2);
            }
            batch.end();
        }
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        candyImage.dispose();
        batImage.dispose();
        pumpkinImage.dispose();
        sound.dispose();
        batch.dispose();
        font.dispose();
    }
}
