package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.Units.UnitType;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class MainMenuScreen extends MenuScreen
{
	Sprite buttonSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	boolean isSettingsOpen;
	SettingsPanel settingsPanel;

	public MainMenuScreen(final HeroesEmblem game)
	{
		super(game);
		this.game.isQuitting = false;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("MenuButton");	
		buttonHeight = Gdx.graphics.getHeight() / 6;
		xOffset = Gdx.graphics.getWidth() / 3;
		yOffset = Gdx.graphics.getHeight() / 6;
		buttonSprite = new Sprite(buttonRegion);
		MusicManager.PlayMenuMusic(this.game.settings.getFloat("musicVolume", .25f));
		this.settingsPanel = new SettingsPanel(game, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 7 / 9, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() * 1 / 9);
		this.game.battleState = null;
		this.game.shopState = null;
	}

	public void draw()
	{
		game.font.setColor(Color.WHITE);
		super.setupDraw();
		this.game.batcher.begin();
		drawContent();
		super.drawBorder();
		this.game.batcher.end();
		if(isSettingsOpen){
			game.shapeRenderer.begin(ShapeType.Filled);
			settingsPanel.drawBackground();
			game.shapeRenderer.end();
			game.batcher.begin();
			settingsPanel.draw();
			game.batcher.end();	
		}
	}
	
	private void drawContent(){
		int titleHeight = xOffset * 52 / 257;
		this.game.batcher.draw(game.sprites.MenuBackdrop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.game.batcher.draw(game.sprites.MenuButton, xOffset, this.yOffset * 5 / 2, xOffset, xOffset * 33 / 122);
		this.game.batcher.draw(game.sprites.MenuButton, xOffset * 7 / 8, this.yOffset, xOffset / 2, xOffset * 33 / 244);
		this.game.batcher.draw(game.sprites.MenuButton, xOffset * 13 / 8, this.yOffset, xOffset / 2, xOffset * 33 / 244);
		game.batcher.draw(game.sprites.MainTitle, xOffset * 3 / 4, Gdx.graphics.getHeight() - (titleHeight * 2), xOffset * 3 / 2, titleHeight * 3 / 2);
		this.game.font.getData().setScale(.66f);
		game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "New Game", xOffset, this.yOffset * 5 / 2 + (buttonHeight - this.game.font.getData().lineHeight * 3 / 4), (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Highscores", xOffset * 7 / 8, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight * 3), (float) this.xOffset / 2, 1, false);
		this.game.font.draw(this.game.batcher, "Settings", xOffset * 13 / 8, this.yOffset + (buttonHeight - this.game.font.getData().lineHeight * 3), (float) this.xOffset / 2, 1, false);
		this.game.font.getData().setScale(.2f);
		this.game.font.draw(this.game.batcher, "Art by Kyle McArthur", Gdx.graphics.getWidth() * 3 / 8, chainSize + shadowSize + this.game.font.getData().lineHeight, Gdx.graphics.getWidth() / 4, 1, false);

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
			if(!isSettingsOpen){
			if ((Gdx.input.getX() > this.xOffset  && Gdx.input.getX() < this.xOffset * 2) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset * 5 / 2 && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset * 5 / 2 + (xOffset * 33 / 122)))
			{
				this.game.setScreen(new ShopScreen(this.game));
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 13 / 8 && Gdx.input.getX() < this.xOffset * 17 / 8) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + (xOffset * 33 / 244)))
			{
				isSettingsOpen = true;
				return;
			}
			
			if ((Gdx.input.getX() > this.xOffset * 7 / 8 && Gdx.input.getX() < this.xOffset * 11 / 8) && (Gdx.graphics.getHeight() - Gdx.input.getY() > this.yOffset && Gdx.graphics.getHeight() - Gdx.input.getY() < this.yOffset + (xOffset * 33 / 244)))
			{
				this.game.setScreen(new HighscoreScreen(this.game, this));
				return;
			}
			}else{
				if (settingsPanel.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())){
					settingsPanel.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				}else{
					isSettingsOpen = false;
				}
			}
		}
	}
}
