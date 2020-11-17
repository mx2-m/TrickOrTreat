package com.rri;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rri.debug.DebugCameraController;
import com.rri.debug.DebugViewportUtils;
import com.rri.debug.MemoryInfo;


import java.util.Iterator;

/**
 * Artwork from https://image.similarpng.com/very-thumbnail/2020/07/Flock-bats-on-transparent-background-PNG.png (background)
 * https://www.pinclipart.com/picdir/middle/422-4221540_best-free-squash-clipart-cute-halloween-pumpkin-design.png (pumpkin)
 * https://png.pngtree.com/png-vector/20190121/ourmid/pngtree-sweets-snack-cartoon-candy-png-image_519737.jpg (candy)
 * https://banner2.cleanpng.com/20190629/bal/kisspng-clip-art-t-shirt-clothing-bat-halloween-iii-cute-purple-halloween-bat-t-shirt-5d180a4fb10048.746448901561856591725.jpg (bat)
 * Dance Of The Sugar Plum Fairy by  Pyotr Ilyich Tchaikovsky (background sound)
 */

public class GameScreen implements Screen {

    //screen
    private OrthographicCamera camera;
    private Viewport viewport;
    private BitmapFont font;

    //graphics
    private SpriteBatch spriteBatch; // lista svih sprajtova za draw
    private Texture batTexture, pumpkinTexture, candyTexture;
    private Texture[] backgrounds;
    private Sound soundCandy;
    private Music backgroundMusic;

    //timing
    private float[] backgroundOffsets = {0, 0, 0, 0}; //za pomjeranje pozadine
    private float backgroundSpeed;

    //game objects
    private Bat bat;
    private Score score;
    private Array<Candy> candies;
    private Array<Pumpkin> pumpkins;

    //pools
    private CandyPool candyPool;
    private PumpkinPool pumpkinPool;

    MemoryInfo momoryInfo;

    //debug
    DebugCameraController dcc;
    ShapeRenderer sr;
    private boolean debug = false;

    //particle effect
    ParticleEffect candyEffect;
    ParticleEffect batMovementL;
    ParticleEffect batMovementR;

    public void spawnCandy() {
        Candy candy = candyPool.obtain();
        candy.bounds.x = MathUtils.random(0, Gdx.graphics.getWidth() - pumpkinTexture.getWidth());
        candy.bounds.y = Gdx.graphics.getHeight();
        candy.bounds.width = pumpkinTexture.getWidth();
        candy.bounds.height = pumpkinTexture.getHeight();
        candies.add(candy);
        Candy.TIME = TimeUtils.nanoTime();

    }

    public void spawnPumpkin() {
        Pumpkin pumpkin = pumpkinPool.obtain();
        pumpkin.bounds.x = MathUtils.random(0, Gdx.graphics.getWidth() - pumpkinTexture.getWidth());
        pumpkin.bounds.y = Gdx.graphics.getHeight();
        pumpkin.bounds.width = pumpkinTexture.getWidth();
        pumpkin.bounds.height = pumpkinTexture.getHeight();
        pumpkins.add(pumpkin);
        Pumpkin.TIME = TimeUtils.nanoTime();

    }


    GameScreen() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        soundCandy = Gdx.audio.newSound(Gdx.files.internal("pick.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sound.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        backgrounds = new Texture[3];
        backgrounds[0] = new Texture("background00.png");
        backgrounds[1] = new Texture("background01.png");
        backgrounds[2] = new Texture("background02.png");

        backgroundSpeed = (float) (Gdx.graphics.getHeight()) / 4;


        batTexture = new Texture("bat1.png");
        candyTexture = new Texture("candy3.png");
        pumpkinTexture = new Texture("pumpkin.png");

        bat = new Bat(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 8), 600, batTexture, batTexture.getWidth(), batTexture.getHeight());
        score = new Score(50, 0);

        candies = new Array<>();
        pumpkins = new Array<>();
        candyPool = new CandyPool();
        pumpkinPool = new PumpkinPool();

        //Debug
        momoryInfo = new MemoryInfo(500);
        dcc = new DebugCameraController();
        dcc.setStartPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        sr = new ShapeRenderer();

        //Partice effect
        candyEffect = new ParticleEffect();
        candyEffect.load(Gdx.files.internal("cb"), Gdx.files.internal(""));
        candyEffect.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        candyEffect.getEmitters().first().flipY();

        batMovementL = new ParticleEffect();
        batMovementL.load(Gdx.files.internal("b5"), Gdx.files.internal(""));
        batMovementL.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batMovementL.getEmitters().first().flipY();

        batMovementR = new ParticleEffect();
        batMovementR.load(Gdx.files.internal("b5"), Gdx.files.internal(""));
        batMovementR.getEmitters().first().setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batMovementR.getEmitters().first().flipY();

    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        //clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        spriteBatch.begin();
        //scrolling background
        renderBackground(delta);



        bat.draw(spriteBatch);
        for (Candy candy : candies)
            spriteBatch.draw(candyTexture, candy.bounds.x, candy.bounds.y);
        for (Pumpkin pumpkin : pumpkins)
            spriteBatch.draw(pumpkinTexture, pumpkin.bounds.x, pumpkin.bounds.y);

        //particle effects
        candyEffect.setPosition(bat.bounds.x, bat.bounds.y + 100);
        candyEffect.update(delta); //move line in update part
        candyEffect.draw(spriteBatch);

        batMovementL.setPosition(bat.bounds.x + 90, bat.bounds.y + 30);
        batMovementL.update(delta);
        batMovementL.draw(spriteBatch);

        batMovementR.setPosition(bat.bounds.x, bat.bounds.y + 50);
        batMovementR.update(delta);
        batMovementR.draw(spriteBatch);


        font.setColor(Color.ORANGE);
        font.draw(spriteBatch, "Score: " + score.candyScore, 800, Gdx.graphics.getHeight() - 10);
        font.setColor(Color.YELLOW);
        font.draw(spriteBatch, "Health: " + score.batHealth, 20, Gdx.graphics.getHeight() - 10);

        spriteBatch.end();

        //debug
        if (Gdx.input.isKeyJustPressed(Input.Keys.F5)) debug = !debug;

        if (debug) {
            momoryInfo.update();
            dcc.handleDebugInput(Gdx.graphics.getDeltaTime());
            dcc.applyTo(camera);
            spriteBatch.begin();
            {
                GlyphLayout layout = new GlyphLayout(font, "FPS:" + Gdx.graphics.getFramesPerSecond());
                font.setColor(Color.YELLOW);
                font.draw(spriteBatch, layout, 800, Gdx.graphics.getHeight() - 50);

                font.setColor(Color.YELLOW);
                font.draw(spriteBatch, "RC:" + spriteBatch.totalRenderCalls, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 20);
                momoryInfo.render(spriteBatch, font);
            }
            spriteBatch.end();

            spriteBatch.totalRenderCalls = 0;

            DebugViewportUtils.drawGrid(viewport, sr, 50);

            //Print rectangles
            sr.setProjectionMatrix(camera.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            {
                sr.setColor(1, 1, 0, 1);

                for (Candy candy : candies) {
                    sr.rect(candy.bounds.x, candy.bounds.y, candyTexture.getWidth(), candyTexture.getHeight());
                }
                for (Pumpkin pumpkin : pumpkins) {
                    sr.rect(pumpkin.bounds.x, pumpkin.bounds.y, pumpkinTexture.getWidth(), pumpkinTexture.getHeight());
                }
                sr.rect(bat.bounds.x, bat.bounds.y, batTexture.getWidth(), batTexture.getHeight());
            }
            sr.end();
        }

        spriteBatch.setProjectionMatrix(camera.combined);

        if (score.batHealth > 0) {

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                bat.commandMoveLeft();
                if (batMovementL.isComplete())
                    batMovementL.reset();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                bat.commandMoveRight();
                if (batMovementR.isComplete())
                    batMovementR.reset();
            }

            if (TimeUtils.nanoTime() - Candy.TIME > Candy.CREATE_CANDY_TIME)
                spawnCandy();
            if (TimeUtils.nanoTime() - Pumpkin.TIME > Pumpkin.CREATE_PUMPKIN_TIME)
                spawnPumpkin();

            for (
                    Iterator<Pumpkin> iter = pumpkins.iterator(); iter.hasNext(); ) {
                Pumpkin pumpkin = iter.next();
                pumpkin.bounds.y -= 400 * Gdx.graphics.getDeltaTime();
                if (pumpkin.bounds.y + pumpkinTexture.getHeight() < 0) {
                    iter.remove(); //From screen
                    pumpkinPool.free(pumpkin);
                }
                if (pumpkin.bounds.overlaps(bat.bounds)) {

                    score.batHealth--;
                    //iter.remove();
                }
            }
            for (Iterator<Candy> iter = candies.iterator(); iter.hasNext(); ) {
                Candy candy = iter.next();
                candy.bounds.y -= 200 * Gdx.graphics.getDeltaTime();
                if (candy.bounds.y + candyTexture.getHeight() < 0) {
                    iter.remove();
                    candyPool.free(candy);

                }
                if (candy.bounds.overlaps(bat.bounds)) {
                    soundCandy.play();
                    score.candyScore++;

                    if (candyEffect.isComplete())
                        candyEffect.reset();

                    if (score.candyScore % 10 == 0) Pumpkin.SPEED += 66; //speeds up
                    iter.remove(); //smart Array enables remove from Array
                    candyPool.free(candy);
                }
            }

        } else {
            spriteBatch.begin();
            {
                font.setColor(Color.RED);
                font.draw(spriteBatch, "GAME OVER", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                backgroundMusic.stop();
            }
            spriteBatch.end();
        }

    }

    private void renderBackground(float deltaTime) {

        backgroundOffsets[0] += deltaTime * backgroundSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundSpeed / 2;

        for (int layer = 0; layer < 3; layer++) {
            if (backgroundOffsets[layer] > Gdx.graphics.getHeight())
                backgroundOffsets[layer] = 0;

            spriteBatch.draw(backgrounds[layer], 0, backgroundOffsets[layer], Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            spriteBatch.draw(backgrounds[layer], 0, backgroundOffsets[layer] - Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // coordinate system specified by the camera.
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        candyTexture.dispose();
        batTexture.dispose();
        pumpkinTexture.dispose();
        backgrounds[0].dispose();
        backgrounds[1].dispose();
        backgrounds[2].dispose();
        soundCandy.dispose();
        backgroundMusic.dispose();
        font.dispose();
        candyEffect.dispose();
        batMovementL.dispose();
        batMovementR.dispose();
    }
}


