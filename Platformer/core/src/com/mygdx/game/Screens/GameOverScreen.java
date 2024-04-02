package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class GameOverScreen implements Screen {
    private Stage stage;
    private Game game; // Keep a reference to the Game object to switch screens
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture replayTexture;
    private TextButton replayButton;
    private Texture replayhoverTexture;
    private Music music;

    public GameOverScreen(final Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage(new ScreenViewport());
        music = MyGdxGame.manager.get("Audio/bgmenu.mp3", Music.class);
        music.setLooping(true);
        music.play();

        // Set font size
        font.getData().setScale(2.0f);

        // Load button texture
        replayTexture = new Texture(Gdx.files.internal("Menu/replaybtn.png"));
        replayhoverTexture = new Texture("Menu/playbtn_hover.png");
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = new SpriteDrawable(new Sprite(replayTexture)); // Set button image

        // Create the button
        replayButton = new TextButton("Replay", textButtonStyle);
        replayButton.setPosition(Gdx.graphics.getWidth() / 2 - replayButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - replayButton.getHeight() / 2);

        // Add a click event to the button
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Restart the game, might need to call a method that resets the game logic
                game.setScreen(new PlayScreen((MyGdxGame) game)); // Assume you have a PlayScreen for gameplay
            }
        });

        // Add the button to the stage
        stage.addActor(replayButton);
    }

    @Override
    public void show() {
        // Set the input processor to the stage
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 3 / 4);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Adjust the viewport based on the new window size
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Implement if your game needs to handle pause
    }

    @Override
    public void resume() {
        // Implement if your game needs to handle resume
    }

    @Override
    public void hide() {
        // Dispose of the resources when the screen is no longer active
        dispose();
    }

    @Override
    public void dispose() {
        // Dispose of the resources used by the screen
        stage.dispose();
        font.dispose();
        batch.dispose();
        replayTexture.dispose();}
}