package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.HUD;
import com.mygdx.game.Sprites.Chara;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;





public class PlayScreen implements Screen {
    private MyGdxGame game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private HUD hud;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Chara player;

    private final float SCREEN_LEFT_BOUND = 2;
    private final float SCREEN_RIGHT_BOUND =10;
    private static final float MAP_SCROLL_SPEED = 1f;
    private boolean canJump = true;









    public PlayScreen(MyGdxGame game){ //Constructor

        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MyGdxGame.V_WIDTH/MyGdxGame.PPM, MyGdxGame.V_HEIGHT/MyGdxGame.PPM, gamecam);
        hud = new HUD(game.batch);

        maploader = new TmxMapLoader();
        map = maploader.load("Level_1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2 , gamePort.getWorldHeight()/2, 0);
        world = new World(new Vector2(0, -10/ MyGdxGame.PPM), true);
        b2dr = new Box2DDebugRenderer();

        // player = new Chara(world, new Vector2(0f, 0,),this);
        player = new Chara(world, new Vector2(0f, 0));
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;




        //for the ground
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2)/MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/MyGdxGame.PPM, rect.getHeight()/2/MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);


        }

        //for the brick
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2)/MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/MyGdxGame.PPM, rect.getHeight()/2/MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

        }
    }

    public void MoveMap(float dt) {

        // Calculate the map movement based on a constant speed
        float mapDeltaX = MAP_SCROLL_SPEED * dt;
        // Update the camera position with the map movement
        gamecam.position.x += mapDeltaX;
        gamecam.update();
        // Update the renderer with the new camera position
        renderer.setView(gamecam);

    }

    @Override
    public void show() {

    }

    public void MoveCamera(float dt){

        gamecam.position.x = gamecam.position.x + player.b2body.getLinearVelocity().x * dt;
        gamecam.update();



    }




    public void handleInput(float dt){


        // Apply gravity
        System.out.println("Player Velocity: " + player.b2body.getLinearVelocity());

        // Apply gravity
        player.b2body.applyForceToCenter(0, -9.8f, true); // Adjust the gravity force as needed

        // Jumping
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && Math.abs(player.b2body.getLinearVelocity().y) < 0.01f) // Ensures the player can only jump if it's not already in the air
            player.b2body.applyLinearImpulse(new Vector2(0, 5f), player.b2body.getWorldCenter(), true); // Adjust the impulse for a higher jump

    }




    public void update(float dt) {

        // Adjust the player's horizontal movement speed to match the map scroll speed
        float playerSpeed = MAP_SCROLL_SPEED; // Adjust the multiplier as needed

        // Apply a constant velocity to the character to make it move horizontally
        player.b2body.setLinearVelocity(playerSpeed, player.b2body.getLinearVelocity().y);

        handleInput(dt); // Keep this line if you want to handle jumping

        MoveMap(dt);
        updateCharacterPosition();
        world.step(1 / 60f, 6, 2);


    }




    public void applyKickstartForce() {
        // Apply a kickstart force to the character if its velocity is below a certain threshold
        if (player.b2body.getLinearVelocity().x < 1.0f) {
            player.b2body.applyForceToCenter(2.0f, 0, true); // Kickstart force
        }
    }

    public void updateCharacterPosition() {
        // Ensure the character stays at a fixed position on the left side of the screen
        float characterX = Math.max(player.b2body.getPosition().x, SCREEN_LEFT_BOUND + player.getWidth() / 2);

        // Update the character's position relative to the map movement
        float characterDeltaX = characterX - player.getX();
        player.translateX(characterDeltaX);

        // Update the character's y-position if needed
        float characterY = player.b2body.getPosition().y;
        player.setPosition(characterX, characterY);


    }


    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr. render (world, gamecam. combined);

        game.batch.setProjectionMatrix(HUD.stage.getCamera().combined);
        hud.stage.draw();

    }







    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();


    }

}