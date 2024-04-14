package com.mygdx.game.Tools;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.*;

public class B2WorldCreator {
    private Array<RollingObstacle> Rolls;

    public B2WorldCreator(PlayScreen screen){

        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        //for the ground
        for(MapObject object: map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2)/ MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/MyGdxGame.PPM, rect.getHeight()/2/MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);


        }
        //for the Walls
        for(MapObject object: map.getLayers().get("Walls").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Walls(screen, rect);




        }
        //for the Brittle Platforms
        for(MapObject object: map.getLayers().get("Bricks").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new BrittlePlatform(screen, rect);


        }
        for(MapObject object: map.getLayers().get("Spikes").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new SpikeObstacle(screen, rect);


        }
        //for the Goal
        for(MapObject object: map.getLayers().get("Goal").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Goal(screen, rect);

        }
        Rolls = new Array<RollingObstacle>();
        for(MapObject object: map.getLayers().get("Obstacles").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Rolls.add(new RollingObstacle(screen, rect.getX()/MyGdxGame.PPM, rect.getY()/MyGdxGame.PPM));


        }
    }
    public Array<RollingObstacle> getRolls() {
        return Rolls;
    }
    public Array<Obstacle> getObstacles(){
        Array<Obstacle> obstacles = new Array<Obstacle>();
        obstacles.addAll(Rolls);
        return obstacles;
    }

}
