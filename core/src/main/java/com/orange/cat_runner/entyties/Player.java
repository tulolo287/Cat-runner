package com.orange.cat_runner.entyties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.orange.cat_runner.screens.GameScreen;

public class Player {
    private final Body player;
    public Texture playerTexture;
    CircleShape circleShape;
    PolygonShape polygonShape;
    public float width;
    public float height;
    private final Vector2[] vertices;
    float stateTime;
    public enum State {
        JUMP,
        WALK,
        IDLE,
        FALL,
        RUN
    }
    State state = State.WALK;
    Animation<TextureRegion> walkAnimation;
    Animation<TextureRegion> runAnimation;
    Animation<TextureRegion> jumpAnimation;
    Animation<TextureRegion> fallAnimation;
    Texture walkSheet;
    TextureRegion currentAnimation;
    private static final int FRAME_COLS = 8, FRAME_ROWS = 51;

    public Player(World world, float width, float height, Vector2 pos){

        walkSheet = new Texture(Gdx.files.internal("Cat-Sheet.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
            MathUtils.floor((float) walkSheet.getWidth() / FRAME_COLS),
            MathUtils.ceil((float) walkSheet.getHeight() / FRAME_ROWS));

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
        System.arraycopy(tmp[4], 0, walkFrames, 0, FRAME_COLS);
        walkAnimation = new Animation<TextureRegion>(.1f, walkFrames);

        TextureRegion[] runFrames = new TextureRegion[4];
        System.arraycopy(tmp[6], 0, runFrames, 0, 4);
        runAnimation = new Animation<TextureRegion>(.07f, runFrames);
        //fallAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        TextureRegion[] jumpFrames = new TextureRegion[4];
        System.arraycopy(tmp[19], 0, jumpFrames, 0, 4);
        jumpAnimation = new Animation<TextureRegion>(.1f, jumpFrames);
        //jumpAnimation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[] fallFrames = new TextureRegion[4];
        System.arraycopy(tmp[20], 0, fallFrames, 0, 4);
        fallAnimation = new Animation<TextureRegion>(.1f, fallFrames);
        //fallAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        stateTime = 0f;

        this.width = width;
        this.height = height;
        this.vertices = new Vector2[4];
        this.vertices[0] = new Vector2(-width, -width);
        this.vertices[1] = new Vector2(-width, .3f);
        this.vertices[2] = new Vector2(.3f, .3f);
        this.vertices[3] = new Vector2(.3f, -width);

        playerTexture = new Texture("cat-image.png");
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);
        player = world.createBody(bodyDef);

        polygonShape = new PolygonShape();
        polygonShape.set(vertices);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = GameScreen.PLAYER;
        fixtureDef.filter.maskBits = GameScreen.PLATFORM | GameScreen.BUTTERFLY;
        player.createFixture(fixtureDef);
        polygonShape.dispose();

/*        circleShape = new CircleShape();
        circleShape.setRadius(.3f);
        circleShape.setPosition(new Vector2(0, 0));
        fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = GameScreen.PLAYER;
        fixtureDef.filter.maskBits = GameScreen.PLATFORM | GameScreen.BUTTERFLY;
        player.createFixture(fixtureDef);
        circleShape.dispose();*/

    }

    public void setFixtureJump(Vector2 pos) {
        polygonShape = (PolygonShape) player.getFixtureList().get(0).getShape();
        this.vertices[0] = new Vector2(-width, 1);
        this.vertices[1] = new Vector2(-width, .5f);
        this.vertices[2] = new Vector2(.3f, .5f);
        this.vertices[3] = new Vector2(.3f, pos.y);
        polygonShape.set(vertices);
    }

    public void setFixtureFall() {
        polygonShape = (PolygonShape) player.getFixtureList().get(0).getShape();
        this.vertices[0] = new Vector2(-width, 0);
        this.vertices[1] = new Vector2(-width, .5f);
        this.vertices[2] = new Vector2(.3f, .5f);
        this.vertices[3] = new Vector2(.3f, -.1f);
        polygonShape.set(vertices);
    }

    public void resetFixture() {
        this.vertices[0] = new Vector2(-width, -width);
        this.vertices[1] = new Vector2(-width, .3f);
        this.vertices[2] = new Vector2(.3f, .3f);
        this.vertices[3] = new Vector2(.3f, -width);
        polygonShape = (PolygonShape) player.getFixtureList().get(0).getShape();
        polygonShape.set(vertices);
    }

    public void setState(State newState) {
        this.state = newState;
    }
    public State getState() {
        return this.state;
    }
    public Body getBody(){
        return player;
    }

    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();

        if (player.getLinearVelocity().y == 0 && GameScreen.GAME_SPEED < .4f) {
            setState(Player.State.WALK);
        } else if (player.getLinearVelocity().y == 0 && GameScreen.GAME_SPEED > .4f) {
            setState(Player.State.RUN);
        } else if (player.getLinearVelocity().y < 0) {
            setState(Player.State.FALL);
        } else if (player.getLinearVelocity().y > 0) {
            setState(Player.State.JUMP);
        }

        if(getState() == Player.State.WALK) {
            currentAnimation = walkAnimation.getKeyFrame(stateTime, true);
            resetFixture();
        } else if (state == Player.State.RUN) {
            resetFixture();
            currentAnimation = runAnimation.getKeyFrame(stateTime, true);
        } else if (state == Player.State.JUMP) {
            setFixtureJump(new Vector2(0, 1f));
            currentAnimation = jumpAnimation.getKeyFrame(stateTime, false);
        } else if (state == Player.State.FALL) {
            setFixtureFall();
            currentAnimation = fallAnimation.getKeyFrame(stateTime, false);
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(currentAnimation, player.getPosition().x - 1.5f * .5f, player.getPosition().y - .4f, 1.5f, 1.5f);
    }
}
