package com.orange.cat_runner.entyties;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Particle {
    float radius;
    Vector2 pos;
    int timer;
    public boolean isDestroy;
    float velocityX;
    float velocityY;
    float r;
    float g;
    float b;
    public Particle(float radius, Vector2 pos) {
        this.velocityX = MathUtils.random(-.1f, .1f);
        this.velocityY = MathUtils.random(-.1f, .1f);
        this.timer = 20;
        this.isDestroy = false;
        this.radius = radius;
        this.pos = pos;
        this.r = MathUtils.random(0, 1);
        this.g = MathUtils.random(0, 1);
        this.b = MathUtils.random(0, 1);
    }

    public void draw(ShapeRenderer sh, OrthographicCamera camera) {
        //sh.setProjectionMatrix(camera.combined);
        sh.begin(ShapeRenderer.ShapeType.Filled);
        //sh.identity();
        if (r == 0 && g == 0 && b == 0) {
            r = 1;
            g = 1;
        }
        sh.setColor(new Color(r, g, b, 1));
        sh.circle(pos.x, pos.y, radius, 100);
        sh.end();
    }

    public void update() {
        if (radius > 0) {
            this.pos.set(pos.x + velocityX, pos.y + velocityY);
            this.radius -= .01f;
        } else {
            this.isDestroy = true;
        }
    }
}
