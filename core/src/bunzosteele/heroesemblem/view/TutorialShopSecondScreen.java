package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class TutorialShopSecondScreen extends MenuScreen
{
	Sprite buttonSprite;
	Sprite exampleSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	float exampleRatio;

	public TutorialShopSecondScreen(final HeroesEmblem game)
	{
		super(game);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion exampleRegion = this.game.textureAtlas.findRegion("UnitStatusExample");
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.buttonSprite = new Sprite(buttonRegion);
		this.exampleSprite = new Sprite(exampleRegion);
		exampleRatio = (float) 224 / 593;
	}

	public void draw()
	{
		super.setupDraw();
		this.game.batcher.begin();
		drawContent();
		this.game.batcher.end();
	}
	
	private void drawContent(){
		super.drawBackground();
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 5 / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(.60f);
		this.game.font.draw(this.game.batcher, "Shop", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Back", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Previous", this.xOffset/2, this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Next", this.xOffset * 5 / 2 , this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.20f);
		this.game.font.draw(this.game.batcher, "Here, you can see the selected unit's stats.\nThe cost of the unit is in the upper right corner.\nBoosted stats are shown in gold.\nReduced stats are shown in red.\nYou can see how much gold you have remaining above the 'Buy' button in the shop.\n Press the gear icon in the upper right corner to open settings.\n Press the icon in the bottom left corner to open more details.", this.xOffset, this.yOffset * 3 - 1 * this.game.font.getData().lineHeight, 3 * this.xOffset, 1, true);
		this.game.batcher.draw(exampleSprite, this.xOffset / 2, this.yOffset + this.buttonHeight / 5, this.yOffset * 2 * exampleRatio, this.yOffset * 2);
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
			if(flippedY >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight)
			{
				checkBackTouch(Gdx.input.getX(), flippedY);
				return;
			}	
			if(flippedY <= this.yOffset)
			{
				checkPreviousTouch(Gdx.input.getX(), flippedY);
				checkNextTouch(Gdx.input.getX(), flippedY);
				return;
			}		
		}else if(Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.BACKSPACE)){
			this.game.setScreen(new MainMenuScreen(this.game));
		}
	}

	private void checkBackTouch(int x, int y){
		if((x >= this.xOffset * 3 - this.game.font.getData().lineHeight && x <= (this.xOffset * 3 - this.game.font.getData().lineHeight) + this.xOffset) && (y >= this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight && y <= this.yOffset * 4 - this.game.font.getData().lineHeight))
			this.game.setScreen(new TutorialMenuScreen(this.game));
	}
	
	private void checkPreviousTouch(int x, int y){
		if((x >= (this.xOffset/2))
				&& (x <= (this.xOffset/2) + this.xOffset)
				&& (y <= this.yOffset)
				&& (y >= this.yOffset - this.buttonHeight))
			this.game.setScreen(new TutorialShopFirstScreen(this.game));
	}
	
	private void checkNextTouch(int x, int y){
		if((x >= (this.xOffset * 5 / 2))
				&& (x <= (this.xOffset * 5 / 2) + this.xOffset)
				&& (y <= this.yOffset)
				&& (y >= this.yOffset - this.buttonHeight))
			this.game.setScreen(new TutorialShopThirdScreen(this.game));
	}
}
