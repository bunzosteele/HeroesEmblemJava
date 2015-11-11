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

public class TutorialBattleSecondScreen extends ScreenAdapter
{
	HeroesEmblem game;
	Sprite buttonSprite;
	Sprite backgroundSprite;
	Sprite grassSprite;
	Sprite sandSprite;
	Sprite woodFloorSprite;
	Sprite wallSprite;
	Sprite metalWallSprite;
	Sprite metalFloorSprite;
	Sprite platingSprite;
	Sprite ashSprite;
	Sprite bridgeSprite;
	Sprite forestSprite;
	Sprite mountainSprite;
	Sprite volcanoSprite;
	Sprite lavaSprite;
	Sprite waterSprite;
	Sprite voidSprite;
	int xOffset;
	int yOffset;
	int buttonHeight;
	float exampleRatio;

	public TutorialBattleSecondScreen(final HeroesEmblem game)
	{
		this.game = game;
		final AtlasRegion buttonRegion = this.game.textureAtlas.findRegion("Button");	
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Grass");
		final AtlasRegion grassRegion = this.game.textureAtlas.findRegion("Grass");
		final AtlasRegion sandRegion = this.game.textureAtlas.findRegion("Sand");
		final AtlasRegion woodFloorRegion = this.game.textureAtlas.findRegion("WoodFloor");
		final AtlasRegion wallRegion = this.game.textureAtlas.findRegion("Wall");
		final AtlasRegion metalWallRegion = this.game.textureAtlas.findRegion("MetalWall");
		final AtlasRegion metalFloorRegion = this.game.textureAtlas.findRegion("MetalFloor");
		final AtlasRegion platingRegion = this.game.textureAtlas.findRegion("Plating");
		final AtlasRegion ashRegion = this.game.textureAtlas.findRegion("Ash");
		final AtlasRegion bridgeRegion = this.game.textureAtlas.findRegion("Bridge");
		final AtlasRegion forestRegion = this.game.textureAtlas.findRegion("Forest");
		final AtlasRegion mountainRegion = this.game.textureAtlas.findRegion("Mountain");
		final AtlasRegion volcanoRegion = this.game.textureAtlas.findRegion("Volcano");
		final AtlasRegion lavaRegion = this.game.textureAtlas.findRegion("Lava");
		final AtlasRegion waterRegion = this.game.textureAtlas.findRegion("Water");
		final AtlasRegion voidRegion = this.game.textureAtlas.findRegion("Void");
		this.buttonHeight = Gdx.graphics.getHeight() / 6;
		this.xOffset = (Gdx.graphics.getWidth()) / 4;
		this.yOffset = Gdx.graphics.getHeight() / 4;
		this.buttonSprite = new Sprite(buttonRegion);
		this.backgroundSprite = new Sprite(backgroundRegion);
		this.grassSprite = new Sprite(grassRegion);
		this.sandSprite = new Sprite(sandRegion);
		this.woodFloorSprite = new Sprite(woodFloorRegion);
		this.wallSprite = new Sprite(wallRegion);
		this.metalWallSprite = new Sprite(metalWallRegion);
		this.metalFloorSprite = new Sprite(metalFloorRegion);
		this.platingSprite = new Sprite(platingRegion);
		this.ashSprite = new Sprite(ashRegion);
		this.bridgeSprite = new Sprite(bridgeRegion);
		this.forestSprite = new Sprite(forestRegion);
		this.mountainSprite = new Sprite(mountainRegion);
		this.volcanoSprite = new Sprite(volcanoRegion);
		this.lavaSprite = new Sprite(lavaRegion);
		this.waterSprite = new Sprite(waterRegion);
		this.voidSprite = new Sprite(voidRegion);
		exampleRatio = (float) 150 / 396;
		game.adsController.hideBannerAd();
	}

	public void draw()
	{
		this.game.font.getData().setScale(.33f);
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.batcher.begin();
		for(int i = 0; i < 33; i++){
			for(int j = 0; j < 19; j++){
				this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 32) * i, (Gdx.graphics.getHeight() / 18) * j, (Gdx.graphics.getWidth() / 32), (Gdx.graphics.getHeight() / 18));
			}
		}
		this.game.batcher.draw(buttonSprite, this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - this.buttonHeight - this.game.font.getData().lineHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset * 5 / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.batcher.draw(buttonSprite, this.xOffset / 2, this.yOffset - this.buttonHeight, this.xOffset, this.buttonHeight);
		this.game.font.getData().setScale(.60f);
		this.game.font.draw(this.game.batcher, "Battle: Terrain", this.xOffset, this.yOffset * 4 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.33f);
		this.game.font.draw(this.game.batcher, "Back", this.xOffset * 3 - this.game.font.getData().lineHeight, this.yOffset * 4 - 2 * this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Previous", this.xOffset/2, this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.draw(this.game.batcher, "Next", this.xOffset * 5 / 2 , this.yOffset - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.font.getData().setScale(.25f);
		this.game.font.draw(this.game.batcher, "Standard", 0, this.yOffset * 3 - this.game.font.getData().lineHeight, (float) this.xOffset, 1, false);
		this.game.batcher.draw(woodFloorSprite, this.xOffset / 4, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(grassSprite, this.xOffset * 3 / 8, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(metalFloorSprite, this.xOffset / 2, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(bridgeSprite, this.xOffset * 5 / 8, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.font.draw(this.game.batcher, "Beneficial", 0, this.yOffset * 3 - this.xOffset / 4 - this.xOffset / 8, (float) this.xOffset, 1, false);
		this.game.batcher.draw(volcanoSprite, this.xOffset * 4 / 16, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset * 3 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(forestSprite, this.xOffset * 6 / 16, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset * 3 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(mountainSprite, this.xOffset * 8 / 16, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset * 3 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(platingSprite, this.xOffset * 10 / 16, this.yOffset * 3 - 2 * this.game.font.getData().lineHeight - this.xOffset * 3 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.font.draw(this.game.batcher, "Detrimental", 0, this.yOffset * 3 - 3 * this.game.font.getData().lineHeight - this.xOffset / 4, (float) this.xOffset, 1, false);
		this.game.batcher.draw(sandSprite, this.xOffset * 3 / 8, this.yOffset * 3 - 3 * this.game.font.getData().lineHeight - this.xOffset /2, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(ashSprite, this.xOffset / 2, this.yOffset * 3 - 3 * this.game.font.getData().lineHeight - this.xOffset / 2, this.xOffset / 8, this.xOffset / 8);
		this.game.font.draw(this.game.batcher, "Blocking", 0, this.yOffset * 3 - 3 * this.game.font.getData().lineHeight - this.xOffset / 2, (float) this.xOffset, 1, false);
		this.game.batcher.draw(wallSprite, this.xOffset * 3 / 16, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset * 5 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(metalWallSprite, this.xOffset * 5 / 16, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset * 5 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(waterSprite, this.xOffset * 7 / 16, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset * 5 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(lavaSprite, this.xOffset * 9 / 16, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset * 5 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.batcher.draw(voidSprite, this.xOffset * 11 / 16, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset * 5 / 8, this.xOffset / 8, this.xOffset / 8);
		this.game.font.getData().setScale(.20f);
		this.game.font.draw(this.game.batcher, "Standard terrain lets units move quickly and has no effect on combat.", this.xOffset, this.yOffset * 3 - this.game.font.getData().lineHeight, 3 * this.xOffset, 1, true);
		this.game.font.draw(this.game.batcher, "Beneficial terrain slows units, but provides defensive and evasive bonuses in combat.", this.xOffset, this.yOffset * 3 - this.xOffset / 4 - this.xOffset / 8, 3 * this.xOffset, 1, true);
		this.game.font.draw(this.game.batcher, "Detrimental terrain greatly slows units, lowers their defenses, and makes them easier to hit.", this.xOffset, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset / 4, 3 * this.xOffset, 1, true);
		this.game.font.draw(this.game.batcher, "Blocking terrain cannot be moved through. Lava and Water will not block ranged attacks.", this.xOffset, this.yOffset * 3 - 4 * this.game.font.getData().lineHeight - this.xOffset / 2, 3 * this.xOffset, 1, true);
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
			this.game.setScreen(new TutorialBattleFirstScreen(this.game));
	}
	
	private void checkNextTouch(int x, int y){
		if((x >= (this.xOffset * 5 / 2))
				&& (x <= (this.xOffset * 5 / 2) + this.xOffset)
				&& (y <= this.yOffset)
				&& (y >= this.yOffset - this.buttonHeight))
			this.game.setScreen(new TutorialBattleThirdScreen(this.game));
	}
}
