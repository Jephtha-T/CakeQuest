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
        buttonTextures = new Texture[5];
        buttonHoverTextures = new Texture[5];
        buttonTextures[0] = new Texture("Levels/1.png");
        buttonHoverTextures[0] = new Texture("Levels/1hover.png");
        buttonTextures[1] = new Texture("Levels/2.png");
        buttonHoverTextures[1] = new Texture("Levels/2hover.png");
        buttonTextures[2] = new Texture("Levels/3.png");
        buttonHoverTextures[2] = new Texture("Levels/3hover.png");
        buttonTextures[3] = new Texture("Levels/4.png");
        buttonHoverTextures[3] = new Texture("Levels/4hover.png");
        buttonTextures[4] = new Texture("Levels/5.png");
        buttonHoverTextures[4] = new Texture("Levels/5hover.png");

        // Create sprites
        splash = new Sprite(splashTexture);
        splash.setSize(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT); // Use predefined dimensions

        levelButtons = new Sprite[5];
        isLevelHovering = new boolean[5];
        float buttonWidth = 100; // Increase the width of the button
        float buttonHeight = 100; // Increase the height of the button
        float buttonSpacing = 1; // Increase the spacing between buttons
        float totalButtonWidth = 1 * buttonWidth + 1 * buttonSpacing;
        float startX = (MyGdxGame.V_WIDTH - totalButtonWidth) / 6; // Horizontal position for upper buttons
        float startXLower = (MyGdxGame.V_WIDTH - totalButtonWidth) / 3; // Horizontal position for lower buttons
        float startY = MyGdxGame.V_HEIGHT * 3 / 5 - buttonHeight / 2; // 3/4 of the screen height for levels 1 to 3
        float startYLower = MyGdxGame.V_HEIGHT / 4 - buttonHeight / 2; // 1/4 of the screen height for levels 4 and 5

        // Initialize and position buttons
        for (int i = 0; i < 5; i++) {
            levelButtons[i] = new Sprite(buttonTextures[i]);
            if (i < 3) {
                levelButtons[i].setPosition(startX + i * (buttonWidth + buttonSpacing), startY);
            } else {
                levelButtons[i].setPosition(startXLower + (i - 3) * (buttonWidth + buttonSpacing), startYLower);
            }
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
        for (int i = 0; i < 5; i++) {
            if ((mousePos.x >= levelButtons[i].getX() && mousePos.x <= levelButtons[i].getX() + levelButtons[i].getWidth()) &&
                    (mousePos.y >= levelButtons[i].getY() && mousePos.y <= levelButtons[i].getY() + levelButtons[i].getHeight())) {
                isLevelHovering[i] = true;
            } else {
                isLevelHovering[i] = false;
            }
        }

        // Draw buttons based on hovering state
        for (int i = 0; i < 5; i++) {
            if (isLevelHovering[i]) {
                batch.draw(buttonHoverTextures[i], levelButtons[i].getX(), levelButtons[i].getY());
            } else {
                batch.draw(buttonTextures[i], levelButtons[i].getX(), levelButtons[i].getY());
            }
        }

        batch.end();

        // Handle input for the first button only
        if (Gdx.input.justTouched() && isLevelHovering[0]) {
            // Button is pressed, transition to PlayScreen
            game.setScreen(new PlayScreen((MyGdxGame) game, "Level_1.tmx")); // Cast the game instance to MyGdxGame
            music.setLooping(false);
            music.stop();
        }
        if (Gdx.input.justTouched() && isLevelHovering[1]) {
            // Button is pressed, transition to PlayScreen
            game.setScreen(new PlayScreen((MyGdxGame) game, "Level_2.tmx")); // Cast the game instance to MyGdxGame
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