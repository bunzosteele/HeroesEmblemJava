package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SettingsScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite activeSmallButtonSprite;
	Sprite inactiveSmallButtonSprite;
	Sprite backgroundSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	float smallButtonSize;
	public static Sound sfxDemo = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	boolean erasingHighscores = false;

	public SettingsScreen(final HeroesEmblem game)
	{
		this.game = game;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion activeSmallButtonRegion = this.game.textureAtlas.findRegion("ActiveSmallButton");
		final AtlasRegion inactiveSmallButtonRegion = this.game.textureAtlas.findRegion("InactiveSmallButton");
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Grass");
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.buttonSprite = new Sprite(buttonRegion);
		this.activeSmallButtonSprite = new Sprite(activeSmallButtonRegion);
		this.inactiveSmallButtonSprite = new Sprite(inactiveSmallButtonRegion);
		this.backgroundSprite = new Sprite(backgroundRegion);
		this.smallButtonSize = this.game.font.getData().lineHeight * 2;
		game.adsController.hideBannerAd();
	}

	public void draw()
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(this.erasingHighscores){
			this.game.shapeRenderer.begin(ShapeType.Filled);
			this.game.shapeRenderer.setColor(0f, 0f, 0f, .5f);
			this.game.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			this.game.shapeRenderer.end();
		}
		this.game.batcher.begin();
		for(int i = 0; i < 33; i++){
			for(int j = 0; j < 19; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 32) * i, (Gdx.graphics.getHeight() / 18) * j, (Gdx.graphics.getWidth() / 32), (Gdx.graphics.getHeight() / 18));
			}
		}
		this.game.batcher.draw(inactiveSmallButtonSprite, this.xOffset * 3 / 2, this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		float sfxVolume = this.game.settings.getFloat("sfxVolume", .5f);
		this.game.batcher.draw(activeSmallButtonSprite, (this.xOffset * 3 / 2) + ((3 * this.game.font.getData().lineHeight) * sfxVolume / .25f), this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		
		this.game.batcher.draw(inactiveSmallButtonSprite, this.xOffset * 3 / 2, this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		
		float musicVolume = this.game.settings.getFloat("musicVolume", .5f);
		this.game.batcher.draw(activeSmallButtonSprite, (this.xOffset * 3 / 2) + ((3 * this.game.font.getData().lineHeight) * musicVolume / .25f), this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		
		this.game.batcher.draw(inactiveSmallButtonSprite, this.xOffset * 3 / 2, this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);	
		this.game.batcher.draw(inactiveSmallButtonSprite, (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		
		float cpuSpeed = this.game.settings.getFloat("cpuSpeed", 1.1f);
		this.game.batcher.draw(activeSmallButtonSprite, ((this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight) - ((3 * this.game.font.getData().lineHeight) * (cpuSpeed - .1f) / .25f), this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4, smallButtonSize, smallButtonSize);
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset, this.game.font.getData().lineHeight / 2, 2 * this.xOffset, 2 * this.game.font.getData().lineHeight);
		this.game.font.getData().setScale(.66f);
		this.game.font.draw(this.game.batcher, "Settings", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Main Menu", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "SFX Volume:", this.xOffset / 4, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) this.xOffset, 2, false);
		this.game.font.draw(this.game.batcher, "0", (this.xOffset * 3 / 2), this.yOffset * 3 - this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "1", (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "2", (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "3", (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "4", (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "Music Volume:", this.xOffset / 4, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) this.xOffset, 2, false);
		this.game.font.draw(this.game.batcher, "0", (this.xOffset * 3 / 2), this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "1", (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "2", (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "3", (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "4", (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "CPU Speed:", this.xOffset / 4, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) this.xOffset, 2, false);
		this.game.font.draw(this.game.batcher, "1", (this.xOffset * 3 / 2), this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "2", (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "3", (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "4", (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "5", (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight, this.yOffset * 3 - 7 * this.game.font.getData().lineHeight, (float) smallButtonSize, 1, false);
		this.game.font.draw(this.game.batcher, "Erase Highscores", this.xOffset, this.game.font.getData().lineHeight * 7 / 4, (float) this.xOffset * 2, 1, false);
		if(this.erasingHighscores){
			for(int i = 0; i < 33; i++){
				for(int j = 0; j < 19; j++){
					this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 32) * i, (Gdx.graphics.getHeight() / 18) * j, (Gdx.graphics.getWidth() / 32), (Gdx.graphics.getHeight() / 18));
				}
			}
			this.game.font.getData().setScale(.66f);
			this.game.font.draw(this.game.batcher, "Erase Highscores?", this.xOffset, this.yOffset * 3, (float) this.xOffset * 2, 1, false);
			this.game.font.getData().setScale(.33f);
			this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 4, this.yOffset * 3 / 2, this.xOffset, this.buttonHeight);
			this.game.batcher.draw(buttonSprite, this.xOffset * 9 / 4, this.yOffset * 3 / 2, this.xOffset, this.buttonHeight);
			this.game.font.draw(this.game.batcher, "Yes", this.xOffset * 3 / 4, this.yOffset * 5 / 2 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
			this.game.font.draw(this.game.batcher, "No", this.xOffset * 9 / 4, this.yOffset * 5 / 2 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		}
		this.game.batcher.end();
	}

	@Override
	public void render(final float delta)
	{
		try
		{
			this.update();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.draw();
	}

	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			int flippedY = Gdx.graphics.getHeight() - Gdx.input.getY();
			if(this.erasingHighscores){
				checkEraseHighscoresTouch(Gdx.input.getX(), flippedY);
			}else{
				if(flippedY >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight)
				{
					checkMainMenuTouch(Gdx.input.getX(), flippedY);
					return;
				}		
				if(flippedY >= this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4)
				{
					checkSfxVolumeTouch(Gdx.input.getX(), flippedY);
					return;
				}
				if(flippedY >= this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4)
				{
					checkMusicVolumeTouch(Gdx.input.getX(), flippedY);
					return;
				}
				if (flippedY >= this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4)
				{
					checkCpuSpeedTouch(Gdx.input.getX(), flippedY);
					return;
				}
				checkEraseHighscoresTouch(Gdx.input.getX(), flippedY);
			}
		}
	}
	
	private void checkEraseHighscoresTouch(int x, int y){
		if(!this.erasingHighscores){
			if(y >= this.game.font.getData().lineHeight / 2 && y <= this.game.font.getData().lineHeight / 2 + 2 * this.game.font.getData().lineHeight){
				if(x >= this.xOffset && x <= this.xOffset * 3){
					this.erasingHighscores = true;
				}
			}
		}else{
			if(y >= this.yOffset * 3 / 2 && y <= this.yOffset * 3 /2 + this.buttonHeight){
				if(x >= this.xOffset * 3 / 4 && x <= this.xOffset * 3 /4 + this.xOffset){
					HighscoreManager.EraseHighscores();
					this.erasingHighscores = false;
				}
				if(x >= this.xOffset * 9 / 4 && x <= this.xOffset * 9 /4 + this.xOffset){
					this.erasingHighscores = false;
				}
			}
		}
	}
	
	private void checkCpuSpeedTouch(int x, int y){
		if((y >= this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4) && (y <= (this.yOffset * 3 - 33 * this.game.font.getData().lineHeight / 4) + smallButtonSize)){
			if((x >= this.xOffset * 3 / 2 && x <= (this.xOffset * 3 / 2) + smallButtonSize)){
				this.game.settings.putFloat("cpuSpeed", 1.1f);
				this.game.settings.flush();
			}
			if((x >= (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("cpuSpeed", .85f);
				this.game.settings.flush();
			}
			if((x >= (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("cpuSpeed", .6f);
				this.game.settings.flush();
			}
			if((x >= (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("cpuSpeed", .35f);
				this.game.settings.flush();
			}
			if((x >= (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("cpuSpeed", .1f);
				this.game.settings.flush();
			}
		}
	}
	
	private void checkMusicVolumeTouch(int x, int y){
		if((y >= this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4) && (y <= (this.yOffset * 3 - 21 * this.game.font.getData().lineHeight / 4) + smallButtonSize)){
			if((x >= this.xOffset * 3 / 2 && x <= (this.xOffset * 3 / 2) + smallButtonSize)){
				this.game.settings.putFloat("musicVolume", 0f);
				this.game.settings.flush();
				MusicManager.nowPlaying.setVolume(0f);
			}
			if((x >= (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("musicVolume", .25f);
				this.game.settings.flush();
				MusicManager.nowPlaying.setVolume(.25f);
			}
			if((x >= (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("musicVolume", .5f);
				this.game.settings.flush();
				MusicManager.nowPlaying.setVolume(.5f);
			}
			if((x >= (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("musicVolume", .75f);
				this.game.settings.flush();
				MusicManager.nowPlaying.setVolume(.75f);
			}
			if((x >= (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("musicVolume", 1f);
				this.game.settings.flush();
				MusicManager.nowPlaying.setVolume(1f);
			}
		}
	}
	
	private void checkSfxVolumeTouch(int x, int y){
		if((y >= this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4) && (y <= (this.yOffset * 3 - 9 * this.game.font.getData().lineHeight / 4) + smallButtonSize)){
			if((x >= this.xOffset * 3 / 2 && x <= (this.xOffset * 3 / 2) + smallButtonSize)){
				this.game.settings.putFloat("sfxVolume", 0f);
				this.game.settings.flush();
				SettingsScreen.sfxDemo.play(0f);
			}
			if((x >= (this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 3 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("sfxVolume", .25f);
				this.game.settings.flush();
				SettingsScreen.sfxDemo.play(.25f);
			}
			if((x >= (this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 6 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("sfxVolume", .5f);
				this.game.settings.flush();
				SettingsScreen.sfxDemo.play(.5f);
			}
			if((x >= (this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 9 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("sfxVolume", .75f);
				this.game.settings.flush();
				SettingsScreen.sfxDemo.play(.75f);
			}
			if((x >= (this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight && x <= ((this.xOffset * 3 / 2) + 12 * this.game.font.getData().lineHeight) + smallButtonSize)){
				this.game.settings.putFloat("sfxVolume", 1f);
				this.game.settings.flush();
				SettingsScreen.sfxDemo.play(1f);
			}
		}
	}
	
	private void checkMainMenuTouch(int x, int y){
		if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (y >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && y <= this.yOffset * 4 - this.game.font.getData().lineHeight))
		this.game.setScreen(new MainMenuScreen(this.game));
	}
	

}
