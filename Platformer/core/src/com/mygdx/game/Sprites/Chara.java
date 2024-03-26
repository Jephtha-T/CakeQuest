package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

public class Chara extends Sprite {
    public World world;
    public Body b2body;
    public Chara(World world){
        this.world = world;
        defineChara();
    }
    public void defineChara(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(50/ MyGdxGame.PPM, 50/MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/MyGdxGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }
}
