package com.orange.cat_runner.system;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.orange.cat_runner.entyties.Butterfly;
import com.orange.cat_runner.screens.GameScreen;

public class WorldListener implements ContactListener {
    private GameScreen gameScreen;
    public WorldListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fb.getFilterData().categoryBits == GameScreen.BUTTERFLY) {
            ((Butterfly)fb.getBody().getUserData()).setDestroy();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
