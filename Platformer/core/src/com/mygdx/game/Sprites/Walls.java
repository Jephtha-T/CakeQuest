package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.MyGdxGame.PPM;

public class Walls extends InteractiveTileObject {
    public PlayScreen screen;
    public  Walls(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        this.screen = screen;

        setCategoryFilter(MyGdxGame.Wall_Bit);
        fixture.setUserData(this);

    }


    public void knocked(){

        Gdx.app.log("Knocked", "Wall");
        //MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
        Chara player;
        player = screen.player;
        player.knocked();
    }

    @Override
    public void onCollision() {
        Gdx.app.log("Ground", "Collision");
        //MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();


    }
}
