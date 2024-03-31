package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Obstacle extends InteractiveTileObject {


    public  Obstacle(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.Brick_Bit);
    }

    @Override
    public void knocked(Chara target) {

    }
    @Override
    public void onCollision() {
        Gdx.app.log("Obstacle", "Collision");
        MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
        setCategoryFilter(MyGdxGame.Destroyed_Bit);
        getCell().setTile(null);
    }
}
