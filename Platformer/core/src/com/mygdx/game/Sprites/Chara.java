package com.mygdx.game.Sprites;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

public class Chara extends Sprite {
    public World world;
    public Body b2body;


    public Chara(World world, Vector2 initialVelocity) {
        // super(screen.getAtlas().findRegion("Running (32x32)"));
        this.world = world;
        defineChara();
        b2body.setLinearVelocity(initialVelocity);

    }

    public void defineChara() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / MyGdxGame.PPM);

        fdef.shape = shape;
        fdef.friction= 0f;
        b2body.createFixture(fdef);
        b2body.setLinearDamping(0f);
        shape.dispose();






    }

}