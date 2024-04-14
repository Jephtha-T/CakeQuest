package com.mygdx.game.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public abstract class InteractiveTileObject extends Sprite {
    protected World world;
    protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;
    public Body b2body;
    protected Game game;

    public InteractiveTileObject(PlayScreen screen, Rectangle bounds){
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        this.screen = screen;
        this.game = screen.game;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX()+ bounds.getWidth()/2)/ MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2)/MyGdxGame.PPM);
        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth()/2/MyGdxGame.PPM, bounds.getHeight()/2/MyGdxGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }



    public abstract void onCollision();
    public abstract void knocked() throws InterruptedException;
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Graphics");
        return layer.getCell((int)(body.getPosition().x * MyGdxGame.PPM / 16), (int)(body.getPosition().y * MyGdxGame.PPM / 16));
    }
}
