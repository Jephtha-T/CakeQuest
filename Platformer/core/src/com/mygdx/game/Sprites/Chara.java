package com.mygdx.game.Sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Chara extends Sprite {
    public enum State{Standing, Falling, Jumping, Running, Knocked};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion charaStand;
    private Animation charaRun;
    private Animation charaJump;
    private TextureRegion charaKnocks;
    private Animation charaKnock;
    private boolean runningRight;
    private float stateTimer;
    private float settime;
    private boolean CharaIsKnocked;
    private PlayScreen screen;


    public Chara(PlayScreen screen) {
        super(screen.getAtlas().findRegion("chara"));
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=1; i<8; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 96+16, 32, 32));
        charaRun= new Animation(0.1f,frames);
        frames.clear();
        for(int i=1; i<9; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 160+16, 32, 32));
        charaJump = new Animation(0.1f, frames);
        frames.clear();
        for(int i=1; i<9; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 224+16, 32, 32));
        charaKnock = new Animation(0.1f, frames);

        charaStand = new TextureRegion(getTexture(), 0, 0+16, 32, 32);

        defineChara();
        setBounds(0, 0, 16/MyGdxGame.PPM, 16/MyGdxGame.PPM);
        setRegion(charaStand);
    }


    public void update(float dt){
            setPosition(b2body.getPosition().x-getWidth()/2, b2body.getPosition().y-getHeight()/2);
            setRegion(getFrame(dt));
    }


    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case Knocked:
                region = (TextureRegion) charaKnock.getKeyFrame(stateTimer, true);
                break;
            case Jumping:
                region = (TextureRegion) charaJump.getKeyFrame(stateTimer);
                break;
            case Running:
                region = (TextureRegion) charaRun.getKeyFrame(stateTimer, true);
                break;
            case Falling:
            case Standing:
            default:
                region = charaStand;
                break;
        }
//        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
//            region.flip(true, false);
//            runningRight= false;
//        }
//        else if((b2body.getLinearVelocity().x>0 || !runningRight) && region.isFlipX()){
//            region.flip(true, false);
//            runningRight = true;
//        }
        stateTimer = currentState == previousState ? stateTimer+dt:0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(CharaIsKnocked)
            return State.Knocked;
        else if(b2body.getLinearVelocity().y>0)
            return State.Jumping;
        else if(b2body.getLinearVelocity().y<0)
            return State.Falling;
        else if(b2body.getLinearVelocity().x!=0)
            return State.Running;
        else return State.Standing;
    }
    public void defineChara() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(150 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / MyGdxGame.PPM);

        fdef.filter.categoryBits = MyGdxGame.Chara_Bit;
        fdef.filter.maskBits = MyGdxGame.Default_Bit | MyGdxGame.Wall_Bit | MyGdxGame.Goal_Bit | MyGdxGame.Obstacle_Bit;

        fdef.shape = shape;
        fdef.friction= 0f;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setLinearDamping(0f);
        shape.dispose();

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(7 / MyGdxGame.PPM, 3 / MyGdxGame.PPM), new Vector2(7 / MyGdxGame.PPM, -3 / MyGdxGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape bottom = new EdgeShape();
        bottom.set(new Vector2(-3 / MyGdxGame.PPM, -6 / MyGdxGame.PPM), new Vector2(3 / MyGdxGame.PPM, -6 / MyGdxGame.PPM));
        fdef.shape = bottom;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("bottom");

    }
    public void knocked() throws InterruptedException {
        CharaIsKnocked = true;

        Gdx.app.log("Knocked", "Wall");
        Filter filter = new Filter();
        MyGdxGame.manager.get("Audio/hit.wav", Sound.class).play();
        b2body.applyLinearImpulse(new Vector2(-1.5f, 0.5f), b2body.getWorldCenter(), true);
        //TimeUnit.SECONDS.sleep(1);
//        settime = stateTimer;
//        if(settime==stateTimer-1)
//            CharaIsKnocked=false;
        new java.util.Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                CharaIsKnocked=false;
                new java.util.Timer().schedule(new TimerTask(){
                    @Override
                    public void run() {
                b2body.applyLinearImpulse(new Vector2(0.5f, 0.0f), b2body.getWorldCenter(), true);
                    }
                },1000,1000);
                //your code here
                //1000*5=5000 millisec. i.e. 5 seconds. you can change accordingly
            }
        },500,500);

        //b2body.applyLinearImpulse(new Vector2(-1.5f, 1f), b2body.getWorldCenter(), true);

        
    }


}