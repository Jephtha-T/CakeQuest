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

public class GameOverScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Sprite splash;
    private Sprite playButton;
    private Sprite LevelButton;
    private Sprite MainButton;
    private Texture buttonTexture;
    private Texture buttonHoverTexture;
    private Texture LevelbuttonTexture;
    private Texture LevelbuttonHoverTexture;
    private Texture MainbuttonTexture;
    private Texture MainbuttonHoverTexture;
    private boolean PlayisHovering;
    private boolean LevelisHovering;
    private boolean MainisHovering;
    private Viewport viewport;
    private Camera camera;
    private Music music;
    public String levelname;

    public GameOverScreen(Game game, String level) {
        this.game = game;
        levelname = level;
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
        Texture splashTexture = new Texture("Menu/GameOverbg.jpg");
        buttonTexture = new Texture("Menu/replaybtn.png");
        buttonHoverTexture = new Texture("Menu/replaybtn_hover.png");
        LevelbuttonTexture = new Texture("Menu/levelbtn.png");  // Use different texture for LevelButton
        LevelbuttonHoverTexture = new Texture("Menu/levelbtn_hover.png");
        MainbuttonTexture = new Texture("Menu/mainbtn.png");  // Use different texture for MainButton
        MainbuttonHoverTexture = new Texture("Menu/mainbtn_hover.png");


        // Create sprites
        splash = new Sprite(splashTexture);
        splash.setSize(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT); // Use predefined dimensions

        playButton = new Sprite(buttonTexture);
        LevelButton = new Sprite(LevelbuttonTexture);
        MainButton = new Sprite(MainbuttonTexture);

        // Set the size of the button
        float buttonWidth = 100; // Adjust the width as needed
        float buttonHeight = 100; // Increase the height as needed
        playButton.setSize(buttonWidth, buttonHeight);
        LevelButton.setSize(buttonWidth, buttonHeight);
        MainButton.setSize(buttonWidth, buttonHeight);


        playButton.setPosition((MyGdxGame.V_WIDTH - 3 * buttonWidth) / 2, 20); // Adjust x-coordinate as needed
        LevelButton.setPosition((MyGdxGame.V_WIDTH - buttonWidth) / 2, 20); // Adjust x-coordinate as needed
        MainButton.setPosition((MyGdxGame.V_WIDTH + buttonWidth) / 2, 20); // Adjust x-coordinate as needed


    }


    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
        playButton.getTexture().dispose();
        buttonHoverTexture.dispose();
        LevelButton.getTexture().dispose();
        LevelbuttonHoverTexture.dispose();
        MainButton.getTexture().dispose();
        MainbuttonHoverTexture.dispose();
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

        // Update button hover state
        updateButtonHoverState(playButton, buttonTexture, buttonHoverTexture, mousePos);
        updateButtonHoverState(LevelButton, LevelbuttonTexture, LevelbuttonHoverTexture, mousePos);
        updateButtonHoverState(MainButton, MainbuttonTexture, MainbuttonHoverTexture, mousePos);

        // Draw buttons
        playButton.draw(batch);
        LevelButton.draw(batch);
        MainButton.draw(batch);
        batch.end();

        // Handle input
        handleInput();
    }

    private void updateButtonHoverState(Sprite button, Texture normalTexture, Texture hoverTexture, Vector3 mousePos) {
        boolean isHovering = button.getBoundingRectangle().contains(mousePos.x, mousePos.y);

        if (button == playButton) {
            PlayisHovering = isHovering;
            if (isHovering) {
                LevelisHovering = false;
                MainisHovering = false;
            }
        } else if (button == LevelButton) {
            LevelisHovering = isHovering;
            if (isHovering) {
                PlayisHovering = false;
                MainisHovering = false;
            }
        } else if (button == MainButton) {
            MainisHovering = isHovering;
            if (isHovering) {
                PlayisHovering = false;
                LevelisHovering = false;
            }
        }

        if (isHovering) {
            button.setTexture(hoverTexture);
        } else {
            button.setTexture(normalTexture);
        }
    }


    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (playButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                game.setScreen(new PlayScreen((MyGdxGame) game, levelname));
            } else if (LevelButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                goToLevelSelect();
            } else if (MainButton.getBoundingRectangle().contains(touchPos.x, touchPos.y)) {
                goToMainMenu();
            }
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
    private void goToMainMenu() {
        game.setScreen(new MenuScreen(game));
        music.setLooping(false);
        music.stop();
    }

    private void goToLevelSelect() {
        game.setScreen(new LevelScreen(game));
        music.setLooping(false);
        music.stop();
    }
}
