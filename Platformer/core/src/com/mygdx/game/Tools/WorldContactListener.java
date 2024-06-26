package com.mygdx.game.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.*;

public class WorldContactListener implements ContactListener {
    public Game game;
    public Music music;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


        switch(cDef){
            //Wall Collision
            case MyGdxGame.Wall_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getUserData()=="head" && fixB.getFilterData().categoryBits == MyGdxGame.Wall_Bit){
                        ((InteractiveTileObject ) fixB.getUserData()).knocked();
                }
                else if(fixB.getUserData()=="head" && fixA.getFilterData().categoryBits == MyGdxGame.Wall_Bit){
                        ((InteractiveTileObject ) fixA.getUserData()).knocked();
                }
                break;

            //Goal Reached
            case MyGdxGame.Goal_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getFilterData().categoryBits == MyGdxGame.Goal_Bit)
                    ((InteractiveTileObject) fixA.getUserData()).onCollision();
                else if(fixB.getFilterData().categoryBits == MyGdxGame.Goal_Bit)
                    ((InteractiveTileObject) fixB.getUserData()).onCollision();
                break;

            //Obstacle Collision
            case MyGdxGame.Obstacle_Bit | MyGdxGame.Chara_Bit:
                if(fixB.getUserData()=="head" && fixA.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {
                        ((Obstacle) fixA.getUserData()).hit();

                }
                else if(fixA.getUserData()=="head" && fixB.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {

                        ((Obstacle) fixB.getUserData()).hit();

                }
                else if(fixA.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {
                    ((Obstacle) fixA.getUserData()).destroy();
                }
                else if(fixB.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit) {
                    ((Obstacle) fixB.getUserData()).destroy();
                }
                break;

            //Brittle Brick Interaction
            case MyGdxGame.Brick_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getUserData()=="bottom" && fixB.getFilterData().categoryBits == MyGdxGame.Brick_Bit)
                    ((InteractiveTileObject) fixB.getUserData()).onCollision();
                else if(fixB.getUserData()=="bottom" && fixA.getFilterData().categoryBits == MyGdxGame.Brick_Bit)
                    ((InteractiveTileObject) fixA.getUserData()).onCollision();
                break;

            case MyGdxGame.Spike_Bit | MyGdxGame.Chara_Bit:
                if(fixA.getUserData()=="bottom" && fixB.getFilterData().categoryBits == MyGdxGame.Spike_Bit) {

                        ((InteractiveTileObject) fixB.getUserData()).knocked();

                }
                else if(fixB.getUserData()=="bottom" && fixA.getFilterData().categoryBits == MyGdxGame.Spike_Bit) {

                        ((InteractiveTileObject) fixA.getUserData()).knocked();

                }
                break;
            //Obstacle Miss
            case MyGdxGame.Obstacle_Bit | MyGdxGame.Wall_Bit:
                if(fixA.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit)
                    ((Obstacle) fixA.getUserData()).miss();
                else if(fixB.getFilterData().categoryBits == MyGdxGame.Obstacle_Bit)
                    ((Obstacle) fixB.getUserData()).miss();
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
