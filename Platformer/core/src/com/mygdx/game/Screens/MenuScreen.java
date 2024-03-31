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

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

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

        // Create sprites
        splash = new Sprite(splashTexture);
        splash.setSize(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT); // Use predefined dimensions

        playButton = new Sprite(buttonTexture);

        // Set the size of the button
        float buttonWidth = 150; // Adjust the width as needed
        float buttonHeight = 150; // Increase the height as needed
        playButton.setSize(buttonWidth, buttonHeight);

        // Position the button in the middle of the screen
        playButton.setPosition(
                (MyGdxGame.V_WIDTH - playButton.getWidth()) / 2,
                MyGdxGame.V_HEIGHT / 3 - playButton.getHeight() / 2 // Center vertically
        );
    }


    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
        playButton.getTexture().dispose();
        buttonHoverTexture.dispose();
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

        playButton.draw(batch);
        batch.end();

        // Handle input
        if (Gdx.input.justTouched() && isHovering) {
            // Button is pressed, transition to PlayScreen
            game.setScreen(new PlayScreen((MyGdxGame) game)); // Cast the game instance to MyGdxGame
            music.setLooping(false);
            music.stop();
        }
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