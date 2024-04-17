package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.PlayScreen;

public class MyGdxGame extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
	public static final short Default_Bit = 1;
	public static final short Chara_Bit = 2;
	public static final short Brick_Bit = 4;
	public static final short Wall_Bit = 8;
	public static final short Goal_Bit = 16;
	public static final short Destroyed_Bit = 32;
	public static final short Obstacle_Bit = 64;
	public static final short Spike_Bit = 128;
	public SpriteBatch batch; //Import all resources

	public static AssetManager manager;
	private Screen previousScreen;


	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Audio/bgmusic.mp3", Music.class);
		manager.load("Audio/bgmenu.mp3", Music.class);
		manager.load("Audio/hit.wav", Sound.class);
		manager.load("Audio/goal.wav", Sound.class);
		manager.load("Audio/break.mp3", Sound.class);
		manager.finishLoading();
		setScreen(new MenuScreen(this));

		//setScreen(new PlayScreen(this)); //Set current screen
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render ()
	{
		super.render(); //Get the current screen to render

	}
	public void setPreviousScreen(Screen previousScreen) {
		this.previousScreen = previousScreen;
	}
	public Screen getPreviousScreen() {
		return previousScreen;
	}
}
