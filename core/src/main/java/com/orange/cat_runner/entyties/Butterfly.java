package com.orange.cat_runner.entyties;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.orange.cat_runner.screens.GameScreen;

public class Butterfly {
    float width;
    float height;
    private final Body butterfly;
    public Texture butterflyTexture;
    CircleShape circleShape;
    public float angle;
    public float curve;
    public boolean isDestroy;
    private float velocityX;
    private Array<Particle> particles;
    public Butterfly(float width, float height, Vector2 pos){
        this.width = width;
        this.height = height;
        this.isDestroy = false;
        this.velocityX = MathUtils.random(-2, 2);

        particles = new Array<>();
        this.curve = MathUtils.random(-1, 1);
        this.angle = 0;

        butterflyTexture = new Texture("butterfly.png");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(pos);
        butterfly = GameScreen.world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        circleShape = new CircleShape();
        circleShape.setRadius(width * .5f);
        circleShape.setPosition(new Vector2(width * .5f, height * .5f));
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = GameScreen.BUTTERFLY;
        fixtureDef.filter.maskBits = GameScreen.PLAYER;
        butterfly.setUserData(this);
        butterfly.createFixture(fixtureDef);
        circleShape.dispose();

    }

    public void setCirclePosition(Vector2 pos) {
        circleShape = (CircleShape) butterfly.getFixtureList().get(0).getShape();
        circleShape.setPosition(pos);
    }

    public Body getBody(){
        return butterfly;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void destroy(World world) {
       /// createParticles();
        world.destroyBody(butterfly);
    }

    private void createParticles() {
        for (int i = 0; i < 5; i++) {
            particles.add(new Particle(.1f, new Vector2(butterfly.getPosition().x, butterfly.getPosition().y)));
        }
    }

    public void update() {
        float shake = MathUtils.random(-3, 3);
        float sin = MathUtils.sin(angle);
        float velocityY = 0;
        if (butterfly.getPosition().y < 0) {
            velocityY = 5;
        }
        if (butterfly.getPosition().y + height > 9) {
            velocityY = -5;
        }
        if (butterfly.getPosition().x + width + 5 < 0) {
            isDestroy = true;
        }
        butterfly.setLinearVelocity(new Vector2(velocityX - (GameScreen.GAME_SPEED * 8) , curve * sin + shake + velocityY));
        angle += .01f;
    }

    public void setDestroy() {
        this.isDestroy = true;
    }

}
