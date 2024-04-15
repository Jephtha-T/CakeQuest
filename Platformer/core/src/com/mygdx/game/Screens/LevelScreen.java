package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;

public class LevelScreen implements Screen {
    private Game game;
    private SpriteBatch batch;
    private Sprite splash;
    private Sprite[] levelButtons;
    private Texture[] buttonTextures;
    private Texture[] buttonHoverTextures;
    private boolean[] isLevelHovering;
    private Viewport viewport;
    private Camera camera;
    private Music music;
    private float startXLower; // Horizontal position for lower buttons

    public LevelScreen(Game game) {
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
        Texture splashTexture = new Texture("Levels/levels_background.png");
        buttonTextures = new Texture[3]; // Adjusted for 3 levels
        buttonHoverTextures = new Texture[3]; // Adjusted for 3 levels
        buttonTextures[0] = new Texture("Levels/1.png");
        buttonHoverTextures[0] = new Texture("Levels/1hover.png");
        buttonTextures[1] = new Texture("Levels/2.png");
        buttonHoverTextures[1] = new Texture("Levels/2hover.png");
        buttonTextures[2] = new Texture("Levels/3.png");
        buttonHoverTextures[2] = new Texture("Levels/3hover.png");

        // Create sprites
        splash = new Sprite(splashTexture);
        splash.setSize(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT); // Use predefined dimensions

        levelButtons = new Sprite[3]; // Adjusted for 3 levels
        isLevelHovering = new boolean[3]; // Adjusted for 3 levels
        float buttonWidth = 100; // Increase the width of the button
        float buttonHeight = 100; // Increase the height of the button
        float buttonSpacing = 1; // Increase the spacing between buttons
        float totalButtonWidth = 1 * buttonWidth + 1 * buttonSpacing;
        float startX = (MyGdxGame.V_WIDTH - totalButtonWidth) / 6; // Horizontal position for upper buttons
       // float startY = MyGdxGame.V_HEIGHT * 3 / 5 - buttonHeight / 2; // 3/4 of the screen height for levels 1 to 3
        float startY = MyGdxGame.V_HEIGHT / 2 - buttonHeight / 2;
        // Initialize and position buttons for the first 3 levels
        for (int i = 0; i < 3; i++) {
            levelButtons[i] = new Sprite(buttonTextures[i]);
            levelButtons[i].setPosition(startX + i * (buttonWidth + buttonSpacing), startY);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
        for (Texture texture : buttonTextures) {
            texture.dispose();
        }
        for (Texture texture : buttonHoverTextures) {
            texture.dispose();
        }
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

        // Check if the mouse is hovering over any button
        for (int i = 0; i < 3; i++) { // Adjusted for 3 levels
            if ((mousePos.x >= levelButtons[i].getX() && mousePos.x <= levelButtons[i].getX() + levelButtons[i].getWidth()) &&
                    (mousePos.y >= levelButtons[i].getY() && mousePos.y <= levelButtons[i].getY() + levelButtons[i].getHeight())) {
                isLevelHovering[i] = true;
            } else {
                isLevelHovering[i] = false;
            }
        }

        // Draw buttons based on hovering state
        for (int i = 0; i < 3; i++) { // Adjusted for 3 levels
            if (isLevelHovering[i]) {
                batch.draw(buttonHoverTextures[i], levelButtons[i].getX(), levelButtons[i].getY());
            } else {
                batch.draw(buttonTextures[i], levelButtons[i].getX(), levelButtons[i].getY());
            }
        }

        batch.end();

        // Handle input for the first 3 buttons only
        if (Gdx.input.justTouched() && isLevelHovering[0]) {
            game.setScreen(new PlayScreen((MyGdxGame) game, "Level_1.tmx"));
            music.setLooping(false);
            music.stop();
        }
        if (Gdx.input.justTouched() && isLevelHovering[1]) {
            game.setScreen(new PlayScreen((MyGdxGame) game, "Level_2.tmx"));
            music.setLooping(false);
            music.stop();
        }
        if (Gdx.input.justTouched() && isLevelHovering[2]) {
            game.setScreen(new PlayScreen((MyGdxGame) game, "Level_3.tmx"));
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
