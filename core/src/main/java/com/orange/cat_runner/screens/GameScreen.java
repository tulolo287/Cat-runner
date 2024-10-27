package com.orange.cat_runner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.orange.cat_runner.entyties.Butterfly;
import com.orange.cat_runner.entyties.Particle;
import com.orange.cat_runner.entyties.Platform;
import com.orange.cat_runner.entyties.Player;
import com.orange.cat_runner.system.InputListener;
import com.orange.cat_runner.system.WorldListener;

public class GameScreen implements Screen {

    Texture backgroundTexture;
    Texture backgroundTexture2;
    Texture backgroundTexture3;
    SpriteBatch spriteBatch;
    public ExtendViewport viewport;
    OrthographicCamera camera;
    Array<Sprite> backgroundSprites;
    Array<Texture> backgroundTextures;
    Rectangle playerRectangle;
    Rectangle platformRectangle;
    float xVelocity = 1f;
    public static float GAME_SPEED = .3f;
    int platformGap = 2;
    public static World world;
    Box2DDebugRenderer debugRenderer;
    Array<Platform> platforms;
    Array<Butterfly> butterflies;
    Array<Particle> particles;
    public static Player player;
    public static final short PLAYER = 1;
    public static final short BUTTERFLY = 2;
    public static final short PLATFORM = 4;
    ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        backgroundTexture = new Texture("background_layer_1.png");
        backgroundTexture2 = new Texture("background_layer_2.png");
        backgroundTexture3 = new Texture("background_layer_3.png");

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        platforms = new Array<>();
        butterflies = new Array<>();
        particles = new Array<>();
        backgroundSprites = new Array<>();
        backgroundTextures = new Array<>();
        playerRectangle = new Rectangle();
        platformRectangle = new Rectangle();

        camera = new OrthographicCamera();
        float width = Gdx.app.getGraphics().getWidth();
        float height = Gdx.app.getGraphics().getHeight();
        float aspect = width / height;
        System.out.println(width);
        viewport = new ExtendViewport(10 * aspect, 10, camera);

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new WorldListener(this));
        createWorld();
        InputListener inputListener = new InputListener();
        Gdx.input.setInputProcessor(inputListener);
    }
    private void createWorld(){
        player = new Player(world, .3f, .2f, new Vector2(6, 5));
        createBackgrounds();
        createPlatforms(7);
    }
    private void createButterflies() {
        for (int i = 0; i < 5; i++) {
            Butterfly butterfly = new Butterfly(.4f, .4f, new Vector2(8, 4));
            butterflies.add(butterfly);
        }
    }

    private void createBackgrounds() {
        backgroundTextures.add(backgroundTexture, backgroundTexture2, backgroundTexture3);
        float worldWidth = viewport.getMinWorldWidth();
        float worldHeight = viewport.getMinWorldHeight();
        for (Texture backgroundTexture : backgroundTextures) {
                Sprite backgroundSprite = new Sprite(backgroundTexture);
                backgroundSprite.setSize(worldWidth, worldHeight);
                backgroundSprite.setX(0);
                backgroundSprite.setY(0);
                backgroundSprites.add(backgroundSprite);
                Sprite backgroundSprite2 = new Sprite(backgroundTexture);
                backgroundSprite2.setSize(worldWidth, worldHeight);
                backgroundSprite2.setX(backgroundSprite.getWidth());
                backgroundSprite2.setY(0);
                backgroundSprites.add(backgroundSprite2);
        }
    }

    private void createPlatforms(int count) {
        for (int i = 0; i < count; i++) {
            Vector2 platformPos = new Vector2((5 + platformGap) * i, 1);
            Platform platform = new Platform(world, 5, .5f, platformPos);
            platforms.add(platform);
        }
    }

    private void createButterfly(Vector2 pos) {
        Butterfly butterfly = new Butterfly(.4f, .4f, pos);
        butterflies.add(butterfly);
    }

    private void createParticles(Body butterfly) {
        System.out.println(butterfly);
        for (int i = 0; i < 5; i++) {
            float radius = MathUtils.random(.1f, .3f);
            particles.add(new Particle(radius, new Vector2(butterfly.getPosition().x, butterfly.getPosition().y)));
        }
    }

    @Override
    public void render(float delta) {
        world.step(1/60f, 6, 2);
        player.update();

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        float worldWidth = viewport.getMinWorldWidth();

        for (int i = 0; i < backgroundSprites.size - 1; i+=2) {
            Sprite backgroundSprite = backgroundSprites.get(i);
            Sprite backgroundSprite2 = backgroundSprites.get(i + 1);
            if (backgroundSprite.getX() < -worldWidth) {
                backgroundSprite.setX(0);
            }
            backgroundSprite.translateX(-xVelocity * i * GAME_SPEED * delta);
            backgroundSprite2.setX(backgroundSprite.getX() + backgroundSprite.getWidth());

            backgroundSprite.draw(spriteBatch);
            backgroundSprite2.draw(spriteBatch);
        }

        for (int i = 0; i < platforms.size; i++) {
            Platform platform = platforms.get(i);
            Platform lastPlatform = platforms.get(platforms.size - 1);
            platform.update();

            if (platform.getBody().getPosition().x > 6 && platform.getBody().getPosition().x < 9) {
                if (player.getBody().getPosition().y < 0) {
                    player.getBody().setTransform(new Vector2(platform.getBody().getPosition().x - platform.getWidth() * .5f, platform.getBody().getPosition().y + .7f), 0);
                    player.getBody().setLinearVelocity(new Vector2(0, 0));
                }
            }
            if (platform.getBody().getPosition().x + platform.getWidth() < 0) {
                platforms.removeIndex(i);
                i--;
                addPlatform(lastPlatform);
            }
            platform.draw(spriteBatch);
        }

        for (int i = 0; i < butterflies.size; i++) {
            Butterfly butterfly = butterflies.get(i);
            butterfly.update();
            if (butterfly.isDestroy) {
                createParticles(butterfly.getBody());
                world.destroyBody(butterfly.getBody());
                butterflies.removeIndex(i);
            }
            spriteBatch.draw(butterfly.butterflyTexture, butterfly.getBody().getPosition().x, butterfly.getBody().getPosition().y, butterfly.getWidth(), butterfly.getHeight());
        }

        player.draw(spriteBatch);

        spriteBatch.end();

        if (particles.size > 0) {
            for (int i = 0; i < particles.size; i++) {
                Particle particle = particles.get(i);
                particle.update();
                particle.draw(shapeRenderer, camera);
                if (particle.isDestroy) {
                    particles.removeIndex(i);
                }
            }
        }
        //debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private void addPlatform(Platform lastPlatform) {
        float worldHeight = viewport.getMinWorldHeight();

        Vector2 newPlatformPos = new Vector2(lastPlatform.getBody().getPosition());
        Vector2 lastPlatformPos = lastPlatform.getBody().getPosition();
        float platformWidth = MathUtils.random(1, 5);

        newPlatformPos.x = lastPlatformPos.x + lastPlatform.getWidth() + platformGap;
        float platformYGap = MathUtils.random(-2f, 2f);

        if (lastPlatformPos.y + lastPlatform.getHeight() + platformYGap + 2 > worldHeight) {
            newPlatformPos.y = lastPlatformPos.y - (platformYGap + lastPlatform.getHeight());
        } else if(lastPlatformPos.y + lastPlatform.getHeight() + platformYGap < 0) {
            newPlatformPos.y = 1;
        } else {
            newPlatformPos.y = lastPlatformPos.y + lastPlatform.getHeight() + platformYGap;
        }

        Platform platform = new Platform(world, platformWidth, .5f, newPlatformPos);
        platforms.add(platform);

        createButterfly(newPlatformPos);
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
        backgroundTexture.dispose();
        backgroundTexture2.dispose();
        backgroundTexture3.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
