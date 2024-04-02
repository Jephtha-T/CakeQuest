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

//        PolygonShape polygon = new PolygonShape();
//        Vector2 size = new Vector2((bounds.x + bounds.width * 0.5f) / MyGdxGame.PPM,
//                (bounds.y + bounds.height * 0.5f ) / MyGdxGame.PPM);
//        polygon.setAsBox(bounds.width * 0.5f / MyGdxGame.PPM,
//                bounds.height * 0.5f / MyGdxGame.PPM,
//                size,
//                0.0f);
//
//        BodyDef bdef = new BodyDef();
//        bdef.type = BodyDef.BodyType.StaticBody;
//        body = world.createBody(bdef);
//
//        PolygonShape pShape = new PolygonShape();
//        pShape.setAsBox(20 / PPM, 25 / PPM);
//
//        FixtureDef fdef= new FixtureDef();
//        fdef.shape = pShape;
//        body.createFixture(fdef).setUserData(this);





    }


    public void knocked() throws InterruptedException {

        Gdx.app.log("Knocked", "Wall");
        MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
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
