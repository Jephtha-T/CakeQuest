package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

public class PauseScreen implements Screen {
    private MyGdxGame game;
    private SpriteBatch batch;
    private Sprite backgroundSprite; // Sprite for the background image
    private Sprite resumeButtonSprite; // Sprite for the resume button
    private Texture backgroundTexture;
    private Texture resumeButtonTexture;
    private Texture resumeButtonHoverTexture;
    private boolean isHovering;
    private Texture retryButtonTexture;
    private Texture retryButtonHoverTexture;
    private Sprite retryButtonSprite; // Sprite for the retry level button
    private boolean isRetryHovering;
    private Texture mainMenuButtonTexture;
    private Texture mainMenuButtonHoverTexture;
    private Sprite mainMenuButtonSprite;
    private boolean isMainMenuHovering;
    private Texture levelSelectButtonTexture;
    private Texture levelSelectButtonHoverTexture;
    private Sprite levelSelectButtonSprite;
    private boolean isLevelSelectHovering;
    private static final float BUTTON_WIDTH = 65; // Adjust as needed
    private static final float BUTTON_HEIGHT = 65; // Adjust as needed


    public PauseScreen(MyGdxGame game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundTexture = new Texture("Menu/menu_background.png"); // Load the background image
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Set size to screen size

        resumeButtonTexture = new Texture("Pause/tile043.png"); // Load the resume button texture
        resumeButtonHoverTexture = new Texture("Pause/tile073.png"); // Load the hover button image
        resumeButtonSprite = new Sprite(resumeButtonTexture);
        resumeButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT); // Set the size of the button
        resumeButtonSprite.setPosition(
                (Gdx.graphics.getWidth() - resumeButtonSprite.getWidth()) / 2 - 50,
                (Gdx.graphics.getHeight() - resumeButtonSprite.getHeight()) / 2 - 20
        );

        retryButtonTexture = new Texture("Pause/tile045.png"); // Load the default button image
        retryButtonHoverTexture = new Texture("Pause/tile075.png"); // Load the hover button image
        retryButtonSprite = new Sprite(retryButtonTexture);
        retryButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT); // Set the size of the button
        retryButtonSprite.setPosition(
                (Gdx.graphics.getWidth() - retryButtonSprite.getWidth()) / 2 + 50,
                (Gdx.graphics.getHeight() - retryButtonSprite.getHeight()) / 2 - 20
        );

        // Load textures for Main Menu button
        mainMenuButtonTexture = new Texture("Pause/tile058.png");
        mainMenuButtonHoverTexture = new Texture("Pause/tile088.png");
        mainMenuButtonSprite = new Sprite(mainMenuButtonTexture);
        mainMenuButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        mainMenuButtonSprite.setPosition(
                (Gdx.graphics.getWidth() - mainMenuButtonSprite.getWidth()) / 2 + 50,
                (Gdx.graphics.getHeight() - mainMenuButtonSprite.getHeight()) / 2 - 110
        );

        // Load textures for Level Select button
        levelSelectButtonTexture = new Texture("Pause/tile046.png");
        levelSelectButtonHoverTexture = new Texture("Pause/tile076.png");
        levelSelectButtonSprite = new Sprite(levelSelectButtonTexture);
        levelSelectButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        levelSelectButtonSprite.setPosition(
                (Gdx.graphics.getWidth() - levelSelectButtonSprite.getWidth()) / 2 - 50,
                (Gdx.graphics.getHeight() - levelSelectButtonSprite.getHeight()) / 2 - 110
        );
    }

    @Override
    public void show() {
        // Logic to show the pause menu UI
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background image
        batch.begin();
        backgroundSprite.draw(batch);

        resumeButtonSprite.draw(batch);
        retryButtonSprite.draw(batch);
        mainMenuButtonSprite.draw(batch);
        levelSelectButtonSprite.draw(batch);


        // Check if the mouse is hovering over the resume button
        if (Gdx.input.getX() >= resumeButtonSprite.getX() &&
                Gdx.input.getX() <= resumeButtonSprite.getX() + resumeButtonSprite.getWidth() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() >= resumeButtonSprite.getY() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() <= resumeButtonSprite.getY() + resumeButtonSprite.getHeight()) {
            isHovering = true;
            resumeButtonSprite.setTexture(resumeButtonHoverTexture); // Change texture to hover state
        } else {
            isHovering = false;
            resumeButtonSprite.setTexture(resumeButtonTexture); // Change texture to default state
        }

        // If the resume button is clicked, resume the game
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isHovering) {
            resumeGame();
        }

        // If "Esc" key is pressed, resume the game
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getPreviousScreen());
        }

        if (Gdx.input.getX() >= retryButtonSprite.getX() &&
                Gdx.input.getX() <= retryButtonSprite.getX() + retryButtonSprite.getWidth() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() >= retryButtonSprite.getY() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() <= retryButtonSprite.getY() + retryButtonSprite.getHeight()) {
            isRetryHovering = true;
            retryButtonSprite.setTexture(retryButtonHoverTexture); // Change texture to hover state
        } else {
            isRetryHovering = false;
            retryButtonSprite.setTexture(retryButtonTexture); // Change texture to default state
        }

        // If the retry button is clicked, restart the level
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isRetryHovering) {
            retryLevel();
        }

        // Check if the mouse is hovering over the Main Menu button
        if (Gdx.input.getX() >= mainMenuButtonSprite.getX() &&
                Gdx.input.getX() <= mainMenuButtonSprite.getX() + mainMenuButtonSprite.getWidth() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() >= mainMenuButtonSprite.getY() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() <= mainMenuButtonSprite.getY() + mainMenuButtonSprite.getHeight()) {
            isMainMenuHovering = true;
            mainMenuButtonSprite.setTexture(mainMenuButtonHoverTexture); // Change texture to hover state
        } else {
            isMainMenuHovering = false;
            mainMenuButtonSprite.setTexture(mainMenuButtonTexture); // Change texture to default state
        }

        // Handle input for Main Menu button
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isMainMenuHovering) {
            goToMainMenu();
        }

        // Check if the mouse is hovering over the level select button
        if (Gdx.input.getX() >= levelSelectButtonSprite.getX() &&
                Gdx.input.getX() <= levelSelectButtonSprite.getX() + levelSelectButtonSprite.getWidth() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() >= levelSelectButtonSprite.getY() &&
                Gdx.graphics.getHeight() - Gdx.input.getY() <= levelSelectButtonSprite.getY() + levelSelectButtonSprite.getHeight()) {
            isLevelSelectHovering = true;
            levelSelectButtonSprite.setTexture(levelSelectButtonHoverTexture); // Change texture to hover state
        } else {
            isLevelSelectHovering = false;
            levelSelectButtonSprite.setTexture(levelSelectButtonTexture); // Change texture to default state
        }

        // If the level select button is clicked, go to the level select screen
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && isLevelSelectHovering) {
            goToLevelSelect();
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize the pause menu UI elements if needed
    }

    @Override
    public void pause() {
        // Logic to pause any ongoing activities in the pause menu
    }

    @Override
    public void resume() {
        // Logic to resume any activities when returning to the pause menu
    }

    @Override
    public void hide() {
        // Logic to hide the pause menu UI
    }

    @Override
    public void dispose() {
        // Dispose any resources used by the pause menu
        batch.dispose();
        backgroundTexture.dispose();
        resumeButtonTexture.dispose();
        retryButtonTexture.dispose();
        mainMenuButtonTexture.dispose();
    }

    private void resumeGame() {
        game.setScreen(game.getPreviousScreen());
    }

    private void retryLevel() {
        // Dispose the current PlayScreen instance
        game.getPreviousScreen().dispose();
        // Create a new PlayScreen instance and set it as the current screen
        game.setScreen(new PlayScreen(game, ((PlayScreen) game.getPreviousScreen()).levelname));
    }

    private void goToMainMenu() {
        game.setScreen(new MenuScreen(game));

    }

    private void goToLevelSelect() {
        game.setScreen(new LevelScreen(game));

    }
}

