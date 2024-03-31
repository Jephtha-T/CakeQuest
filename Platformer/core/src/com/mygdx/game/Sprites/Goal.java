package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Goal extends InteractiveTileObject {

    public  Goal(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.Goal_Bit);
    }

    @Override
    public void knocked(Chara target) {

    }

    @Override
    public void onCollision() {
        Gdx.app.log("Goal", "Collision");
        MyGdxGame.manager.get("Audio/goal.wav", Sound.class).play();
        //Show Victory Screen


    }
}
