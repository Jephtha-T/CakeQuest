package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class SpikeObstacle extends InteractiveTileObject {
    public PlayScreen screen;
    public  SpikeObstacle(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        this.screen = screen;

        setCategoryFilter(MyGdxGame.Spike_Bit);
        fixture.setUserData(this);

    }


    public void knocked() throws InterruptedException {

        Gdx.app.log("Knocked", "Wall");
        MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
        Chara player;
        player = screen.player;
        player.knockedup();
    }

    @Override
    public void onCollision() {
        Gdx.app.log("Ground", "Collision");
        //MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();


    }
}
