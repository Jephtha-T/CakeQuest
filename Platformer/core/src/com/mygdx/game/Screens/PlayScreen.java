package com.mygdx.game.Screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;


public class PlayScreen implements Screen {
    public MyGdxGame game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private HUD hud;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Chara player;
    public Music music;
    private final float SCREEN_LEFT_BOUND = 2;
    private final float SCREEN_RIGHT_BOUND =10;
    private static final float MAP_SCROLL_SPEED = 1f;
    private boolean canJump = true;


    public PlayScreen(MyGdxGame game){ //Constructor
        atlas = new TextureAtlas("CharaSprites/Chara.atlas");
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
        player = new Chara(this);
        new B2WorldCreator(this);

        world.setContactListener(new WorldContactListener());

        music = MyGdxGame.manager.get("Audio/bgmusic.mp3", Music.class);
        music.setLooping(true);
        music.play();
    }

    public TextureAtlas getAtlas(){
        return atlas;
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
        float playerSpeed = MAP_SCROLL_SPEED; // Adjust the multiplier as needed
        // Apply gravity
        //System.out.println("Player Velocity: " + player.b2body.getLinearVelocity());
        // Apply gravity
        player.b2body.applyForceToCenter(0, -9.8f, true); // Adjust the gravity force as needed
        if(player.currentState != Chara.State.Knocked) {
            player.b2body.setLinearVelocity(playerSpeed, player.b2body.getLinearVelocity().y);
            // Jumping
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && Math.abs(player.b2body.getLinearVelocity().y) < 0.01f) // Ensures the player can only jump if it's not already in the air
                player.b2body.applyLinearImpulse(new Vector2(0, 3.3f), player.b2body.getWorldCenter(), true); // Adjust the impulse for a higher jump
        }
    }


    public void update(float dt) {
        // Adjust the player's horizontal movement speed to match the map scroll speed
        // Apply a constant velocity to the character to make it move horizontally
        //player.b2body.setLinearVelocity(playerSpeed, player.b2body.getLinearVelocity().y);
        handleInput(dt); // Keep this line if you want to handle jumping

        MoveMap(dt);
        updateCharacterPosition();
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        hud.update(dt);
        if(player.b2body.getPosition().x<(gamecam.position.x-(gamePort.getWorldWidth()/2)) || player.b2body.getPosition().y<(gamecam.position.y-(gamePort.getWorldHeight()/2))){
            //Show GameoverScreen
            game.setScreen(new MenuScreen((MyGdxGame) game));
            Gdx.app.log("Chara", "Out of Bounds");
            music.setLooping(false);
            music.stop();
        }

    }



//    public void applyKickstartForce() {
//        // Apply a kickstart force to the character if its velocity is below a certain threshold
//        if (player.b2body.getLinearVelocity().x < 1.0f) {
//            player.b2body.applyForceToCenter(2.0f, 0, true); // Kickstart force
//        }
//    }


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
        //Clear Screen to Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Render Map
        renderer.render();
        //Redner Box2D Debug Lines
        b2dr. render (world, gamecam. combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        //Set batch to draw what the HUD Camera sees
        game.batch.setProjectionMatrix(HUD.stage.getCamera().combined);
        hud.stage.draw();

    }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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