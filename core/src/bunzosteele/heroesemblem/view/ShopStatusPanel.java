package bunzosteele.heroesemblem.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;

public class ShopStatusPanel{
	HeroesEmblem game;
	ShopState state;
	int currentFrame = 1;
	
	public ShopStatusPanel(HeroesEmblem game, ShopState state){
		this.game = game;
		this.state = state;
		Timer.schedule(new Task(){
			@Override
			public void run(){
				currentFrame++;
				if(currentFrame > 3)
					currentFrame = 1;
			}
		},0,1/3.0f);
	}
	
	public void draw(){
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(.7f,.3f,.1f,1);	
		game.shapeRenderer.rect(0, Gdx.graphics.getHeight()/ 6, Gdx.graphics.getWidth()/6, 5 * Gdx.graphics.getHeight()/ 6);
		game.shapeRenderer.end();
		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		AtlasRegion shopkeeperRegion = textureAtlas.findRegion("Shopkeeper-" + currentFrame);
		Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		AtlasRegion goldRegion = textureAtlas.findRegion("gold");
		Sprite goldSprite = new Sprite(goldRegion);
		float scaledSize = Gdx.graphics.getWidth()/18;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = (int) (scaledSize);
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		game.batcher.begin();
		game.batcher.draw(shopkeeperSprite, 0, Gdx.graphics.getHeight() - scaledSize, scaledSize, scaledSize);
		game.batcher.draw(goldSprite, 0, Gdx.graphics.getHeight()/6, scaledSize, scaledSize);
		font.draw(game.batcher, "" + state.gold, scaledSize, Gdx.graphics.getHeight()/6 + scaledSize, scaledSize * 2, 1, false);
		game.batcher.end();
		font.dispose();
		textureAtlas.dispose();
	}
}
