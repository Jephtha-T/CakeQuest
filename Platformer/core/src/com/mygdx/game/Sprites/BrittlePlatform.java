package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class BrittlePlatform extends InteractiveTileObject {


    public  BrittlePlatform(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.Brick_Bit);
    }
    @Override
    public void knocked() {

    }
    @Override
    public void onCollision() {
        Gdx.app.log("Brittle", "Collision");
        MyGdxGame.manager.get("Audio/break.mp3", Sound.class).play();
        setCategoryFilter(MyGdxGame.Destroyed_Bit);
        getCell().setTile(null);
    }
}
