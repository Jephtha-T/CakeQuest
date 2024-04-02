package com.mygdx.game.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Chara;
import com.mygdx.game.Sprites.InteractiveTileObject;
import com.mygdx.game.Sprites.Walls;

public class WorldContactListener implements ContactListener {
    public Game game;
    public Music music;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch(cDef){

            case MyGdxGame.Wall_Bit | MyGdxGame.Chara_Bit:
            if(fixA.getUserData()=="head" && fixB.getFilterData().categoryBits == MyGdxGame.Wall_Bit){
                Gdx.app.log("Chara", fixA.getUserData().toString());
                Gdx.app.log("Wall", fixB.getUserData().toString());
                Gdx.app.log("Wall", fixA.getUserData().toString());
                //((Chara) fixA.getUserData()).land();
                try {
                    ((InteractiveTileObject ) fixB.getUserData()).knocked();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(fixB.getUserData()=="head" && fixA.getFilterData().categoryBits == MyGdxGame.Wall_Bit){
                Gdx.app.log("Chara", fixB.getUserData().toString());
                Gdx.app.log("Wall", fixA.getUserData().toString());
                //((Chara) fixB.getUserData()).land();
                try {
                    ((InteractiveTileObject ) fixA.getUserData()).knocked();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            break;
            case MyGdxGame.Goal_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getFilterData().categoryBits == MyGdxGame.Goal_Bit)
                    ((InteractiveTileObject) fixA.getUserData()).onCollision();

                else if(fixB.getFilterData().categoryBits == MyGdxGame.Goal_Bit)
                    ((InteractiveTileObject) fixB.getUserData()).onCollision();
                break;
            case MyGdxGame.Obstacle_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {
                    try {
                        ((InteractiveTileObject) fixA.getUserData()).knocked();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(fixB.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {
                    try {
                        ((InteractiveTileObject)fixB.getUserData()).knocked();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case MyGdxGame.Brick_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getUserData()=="bottom" && fixB.getFilterData().categoryBits == MyGdxGame.Brick_Bit)
                    ((InteractiveTileObject) fixB.getUserData()).onCollision();
                else if(fixB.getUserData()=="bottom" && fixA.getFilterData().categoryBits == MyGdxGame.Brick_Bit)
                    ((InteractiveTileObject) fixA.getUserData()).onCollision();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
