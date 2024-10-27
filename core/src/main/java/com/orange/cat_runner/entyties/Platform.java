package com.orange.cat_runner.entyties;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.orange.cat_runner.screens.GameScreen;

public class Platform implements Disposable {
    Texture platformTexture;
    Body platform;
    private float width;
    private float height;
    public float scale;
    public boolean middle;

    public Platform(World world, float width, float height, Vector2 pos) {
        platformTexture = new Texture("platform.png");

        this.width = width;
        this.height = height;
        this.scale = width / height;
        this.middle = false;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.gravityScale = 0;
        bodyDef.position.set(pos);
        platform = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width * .5f, height * .5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;
        fixtureDef.density = 0;
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = GameScreen.PLATFORM;
        //fixtureDef.filter.maskBits = GameScreen.PLAYER;
        platform.createFixture(fixtureDef);
        polygonShape.dispose();
    }

    public Body getBody() {
        return platform;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(platformTexture, platform.getPosition().x - width * .5f, platform.getPosition().y - height * .5f, width, height, width * .2f, 1, 0, 0);
    }
    public void update() {
        platform.setLinearVelocity(new Vector2(-GameScreen.GAME_SPEED * 8f, 0));
    }

    @Override
    public void dispose() {
        platformTexture.dispose();
    }
}
