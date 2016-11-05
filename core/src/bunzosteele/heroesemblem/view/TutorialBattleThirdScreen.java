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

public class TutorialBattleThirdScreen extends MenuScreen
{
	Sprite buttonSprite;
	Sprite exampleSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	float exampleRatio;

	public TutorialBattleThirdScreen(final HeroesEmblem game)
	{
		super(game);
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.buttonSprite = new Sprite(buttonRegion);
		exampleRatio = (float) 87 / 858;
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
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(.60f);
		this.game.font.draw(this.game.batcher, "Battle: Tips", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Back", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Previous", this.xOffset * 3 / 2, this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.20f);
		this.game.font.draw(this.game.batcher, "Elevated terrain can block the line of sight of ranged units, unless they too are elevated.\n\nDefeating enemies gives you experience. Use experience to level up.\n\nGold earned each battle increases with the difficulty of the battle, and the average level of your army.\n\nActive combat abilities cannot miss.", this.xOffset / 2, this.yOffset * 3 - 1 * this.game.font.getData().lineHeight, 3 * this.xOffset, 1, true);
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
		if((x >= (this.xOffset * 3 / 2))
				&& (x <= (this.xOffset * 3 / 2) + this.xOffset)
				&& (y <= this.yOffset)
				&& (y >= this.yOffset - this.buttonHeight))
			this.game.setScreen(new TutorialBattleSecondScreen(this.game));
	}
}
