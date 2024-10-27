package com.orange.cat_runner.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.orange.cat_runner.Main;
import com.orange.cat_runner.entyties.Player;
import com.orange.cat_runner.screens.GameScreen;

public class InputListener implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenX < Gdx.graphics.getWidth() * .5f) {
            GameScreen.GAME_SPEED = .7f;
        } else {
            if(GameScreen.player.getState().equals(Player.State.RUN) || GameScreen.player.getState().equals(Player.State.WALK)) {
                GameScreen.player.getBody().applyForceToCenter(new Vector2(0, 350), true);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenX < Gdx.graphics.getWidth() * .5f) {
            GameScreen.GAME_SPEED = .3f;
        }
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
