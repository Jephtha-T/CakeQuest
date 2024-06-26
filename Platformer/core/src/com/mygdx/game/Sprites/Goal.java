package com.mygdx.game.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.LevelCompleteScreen;
import com.mygdx.game.Screens.LevelScreen;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.Screens.MenuScreen.volumeLevel;

public class Goal extends InteractiveTileObject {

    private Music music;


    public  Goal(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.Goal_Bit);
    }

    @Override
    public void knocked() {

    }

    @Override
    public void onCollision() {

        Gdx.app.log("Goal", "Collision");
        MyGdxGame.manager.get("Audio/goal.wav", Sound.class).play(volumeLevel);
        MyGdxGame game;
        game = screen.game;
        Music music;
        music = screen.music;
        game.setScreen(new LevelCompleteScreen(game, PlayScreen.levelname));
        if(PlayScreen.levelname =="Level_1.tmx")
            LevelScreen.level1c = true;
        if(PlayScreen.levelname =="Level_2.tmx")
            LevelScreen.level2c = true;
        if(PlayScreen.levelname =="Level_3.tmx")
            LevelScreen.level3c = true;
        music.setLooping(false);
        music.stop();

        //Show Victory Screen


    }
}
