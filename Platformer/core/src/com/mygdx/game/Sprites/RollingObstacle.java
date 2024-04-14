package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;


public class RollingObstacle extends Obstacle {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public RollingObstacle(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("bullet"), 2, 2, 16, 16);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / MyGdxGame.PPM, 16 / MyGdxGame.PPM);
        setToDestroy = false;
        destroyed = false;

    }
    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
    }
    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.Obstacle_Bit;
        fdef.filter.maskBits = MyGdxGame.Chara_Bit |
                MyGdxGame.Wall_Bit |
                MyGdxGame.Default_Bit |
                MyGdxGame.Brick_Bit;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    @Override
    public void miss() {
        setToDestroy = true;
    }

    @Override
    public void hit() throws InterruptedException {
        Gdx.app.log("Knocked", "RollingObstacle");
        MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
        Chara player;
        player = screen.player;
        player.knocked();
        setToDestroy = true;
    }
    public void destroy() {
        setToDestroy = true;
    }

}
