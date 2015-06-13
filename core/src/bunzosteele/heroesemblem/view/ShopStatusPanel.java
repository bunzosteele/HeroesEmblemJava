package bunzosteele.heroesemblem.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	int xOffset;
	int yOffset;
	int width;
	int height;
	
	public ShopStatusPanel(HeroesEmblem game, ShopState state, int width, int height, int yOffset){
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
		xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}
	
	public void draw(){
		AtlasRegion shopkeeperRegion = game.textureAtlas.findRegion("Shopkeeper-" + currentFrame);
		Sprite shopkeeperSprite = new Sprite(shopkeeperRegion);
		AtlasRegion goldRegion = game.textureAtlas.findRegion("Gold");
		Sprite goldSprite = new Sprite(goldRegion);
		float scaledSize = (width/4);
		game.batcher.draw(shopkeeperSprite, xOffset, Gdx.graphics.getHeight() - scaledSize, scaledSize, scaledSize);
		game.batcher.draw(goldSprite, xOffset, yOffset + (game.font.getData().lineHeight - scaledSize) /2, scaledSize, scaledSize);
		game.font.setColor(Color.WHITE);
		game.font.getData().setScale(.4f);
		game.font.draw(game.batcher, "" + state.gold, scaledSize, yOffset + game.font.getData().lineHeight, scaledSize * 2, 1, false);
	}
	
	public void drawBackground(){
		game.shapeRenderer.setColor(.6f,.3f,.1f,1);	
		game.shapeRenderer.rect(xOffset, yOffset, width, height);
	}
		
	public boolean isTouched(float x, float y){
		if(x >= xOffset && x < xOffset + width){
			if( y >= yOffset && y < yOffset + height){
				return true;
			}
		}
		return false;
	}
	
	public void processTouch(float x, float y){
		state.selected = null;
	}
}
