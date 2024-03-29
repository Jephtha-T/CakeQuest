package com.mygdx.game.Sprites;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Chara extends Sprite {
    public enum State{Standing, Falling, Jumping, Running};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion charaStand;
    private Animation charaRun;
    private Animation charaJump;
    private boolean runningRight;
    private float stateTimer;


    public Chara(World world, PlayScreen screen) {
        // super(screen.getAtlas().findRegion("Running (32x32)"));
        super(screen.getAtlas().findRegion("chara"));
        this.world = world;
        currentState = State.Standing;
        previousState = State.Standing;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i=1; i<8; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 96, 32, 32));
        charaRun= new Animation(0.1f,frames);
        frames.clear();
        for(int i=1; i<9; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 160, 32, 32));
        charaJump = new Animation(0.1f, frames);

        charaStand = new TextureRegion(getTexture(), 0, 0, 32, 32);

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
        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight= false;
        }
        else if((b2body.getLinearVelocity().x>0 || !runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer+dt:0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if(b2body.getLinearVelocity().y>0)
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

        fdef.shape = shape;
        fdef.friction= 0f;
        b2body.createFixture(fdef);
        b2body.setLinearDamping(0f);
        shape.dispose();






    }

}