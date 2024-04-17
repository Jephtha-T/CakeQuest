package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.math.MathUtils;


public class MenuScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Sprite splash;
    private Sprite playButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private boolean isHovering;
    private Viewport viewport;
    private Camera camera;
    private Music music;
    private Stage stage;
    private Sprite volumeButton;
    private boolean isVolumeButtonHovering;
    public static float volumeLevel = 0.5f; // Initial volume level
    private Texture volumeUpTexture;
    private Texture volumeDownTexture;
    private Sprite volumeUpButton;
    private Sprite volumeDownButton;
    private boolean isVolumeButtonClicked = false;
    private Texture volumeLowTexture;
    private Texture volumeMediumTexture;
    private Texture volumeHighTexture;


    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport()); // Use ScreenViewport for flexible viewport setup


        Gdx.input.setInputProcessor(stage);

        // Create camera and viewport
        camera = new OrthographicCamera();
        viewport = new FillViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, camera); // Use predefined dimensions
        viewport.apply();
        music = MyGdxGame.manager.get("Audio/bgmenu.mp3", Music.class);
        music.setLooping(true);
        music.play();

        // Load textures
        Texture splashTexture = new Texture("Menu/menu_background.png");
        buttonTexture = new Texture("Menu/playbtn.png");
        buttonHoverTexture = new Texture("Menu/playbtn_hover.png");
        volumeUpTexture = new Texture("Menu/Up-Arrow.png");
        volumeDownTexture = new Texture("Menu/Down-Arrow.png");
        volumeLowTexture = new Texture("Menu/lowvol.png");
        volumeMediumTexture = new Texture("Menu/mediumvol.png");
        volumeHighTexture = new Texture("Menu/highvol.png");

        // Create sprites
        splash = new Sprite(splashTexture);
        splash.setSize(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT); // Use predefined dimensions

        playButton = new Sprite(buttonTexture);
        volumeUpButton = new Sprite(volumeUpTexture);
        volumeDownButton = new Sprite(volumeDownTexture);

        float buttonSize = 13;
        volumeUpButton.setSize(buttonSize, buttonSize);
        volumeDownButton.setSize(buttonSize, buttonSize);

        // Set the size of the button
        float buttonWidth = 150; // Adjust the width as needed
        float buttonHeight = 150; // Increase the height as needed
        playButton.setSize(buttonWidth, buttonHeight);

        // Position the button in the middle of the screen
        playButton.setPosition(
                (MyGdxGame.V_WIDTH - playButton.getWidth()) / 2,
                MyGdxGame.V_HEIGHT / 3 - playButton.getHeight() / 2 // Center vertically
        );

        //Create Volume Button
        Texture volumeButtonTexture = new Texture("Menu/highvol.png");
        volumeButton = new Sprite(volumeButtonTexture);

        float vbuttonWidth = 13; // Adjust the width as needed
        float vbuttonHeight = 13; // Adjust the height as needed
        volumeButton.setSize(vbuttonWidth, vbuttonHeight);
        volumeButton.setPosition(5, 4); // Adjust the position as needed

        // Set the position of the volume buttons
        volumeUpButton.setPosition(47, 4);
        volumeDownButton.setPosition(26, 4);


    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
        playButton.getTexture().dispose();
        buttonHoverTexture.dispose();
        volumeButton.getTexture().dispose();
        volumeLowTexture.dispose();
        volumeMediumTexture.dispose();
        volumeHighTexture.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        splash.draw(batch);
        playButton.draw(batch);
        volumeButton.draw(batch);

        // Convert mouse coordinates to world coordinates
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        // Check if the mouse is hovering over the button
        if (mousePos.x >= playButton.getX() &&
                mousePos.x <= playButton.getX() + playButton.getWidth() &&
                mousePos.y >= playButton.getY() &&
                mousePos.y <= playButton.getY() + playButton.getHeight()) {
            isHovering = true;
            playButton.setTexture(buttonHoverTexture);
        } else {
            isHovering = false;
            playButton.setTexture(buttonTexture);
        }

        // Draw volume up and down buttons only if volume button is clicked
        if (isVolumeButtonClicked) {
            volumeUpButton.draw(batch);
            volumeDownButton.draw(batch);
        }

        // Set the appropriate volume button texture based on volume level
        if (volumeLevel == 0.00f) {
            volumeButton.setTexture(volumeLowTexture);
        } else if (volumeLevel <= 0.33f) {
            volumeButton.setTexture(volumeMediumTexture);
        } else {
            volumeButton.setTexture(volumeHighTexture);
        }


        batch.end();

        // Update the stage
        stage.act(delta);

        // Draw the stage
        stage.draw();

        // Handle input
        if (Gdx.input.justTouched() && isHovering) {
            // Button is pressed, transition to PlayScreen
            game.setScreen(new LevelScreen((MyGdxGame) game)); // Cast the game instance to MyGdxGame
            music.setLooping(false);
            music.stop();
        } else if (Gdx.input.justTouched() && mousePos.x >= volumeUpButton.getX() && mousePos.x <= volumeUpButton.getX() + volumeUpButton.getWidth() &&
                mousePos.y >= volumeUpButton.getY() && mousePos.y <= volumeUpButton.getY() + volumeUpButton.getHeight()) {
            // Increase volume when volume up button is clicked
            adjustVolume(true);
        } else if (Gdx.input.justTouched() && mousePos.x >= volumeDownButton.getX() && mousePos.x <= volumeDownButton.getX() + volumeDownButton.getWidth() &&
                mousePos.y >= volumeDownButton.getY() && mousePos.y <= volumeDownButton.getY() + volumeDownButton.getHeight()) {
            // Decrease volume when volume down button is clicked
            adjustVolume(false);
        } else if (Gdx.input.justTouched() && mousePos.x >= volumeButton.getX() &&
                mousePos.x <= volumeButton.getX() + volumeButton.getWidth() &&
                mousePos.y >= volumeButton.getY() &&
                mousePos.y <= volumeButton.getY() + volumeButton.getHeight()) {
            // Toggle the state of the volume button
            isVolumeButtonClicked = !isVolumeButtonClicked;
        }
        music.setVolume(volumeLevel);

    }

    private void adjustVolume(boolean increase) {
        float volumeIncrement = 0.1f;

        // Increase or decrease volume based on the button clicked
        if (increase) {
            volumeLevel += volumeIncrement;
        } else {
            volumeLevel -= volumeIncrement;
        }

        // Ensure volume level stays within bounds
        volumeLevel = MathUtils.clamp(volumeLevel, 0.0f, 1.0f);

        // Set music volume
        music.setVolume(volumeLevel);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide(){
}
}