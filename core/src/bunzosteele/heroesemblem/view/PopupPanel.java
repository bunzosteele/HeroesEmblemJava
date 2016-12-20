package bunzosteele.heroesemblem.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import bunzosteele.heroesemblem.HeroesEmblem;

public class PopupPanel {
	
	HeroesEmblem game;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int chainSize;
	int shadowSize;
	
	public PopupPanel(HeroesEmblem game, int width, int height, int xOffset, int yOffset){
		this.game = game;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.chainSize = Gdx.graphics.getWidth() / 80;
		this.shadowSize = chainSize / 3; 
	}
	
	public void drawBackground(boolean isFaded)
	{
		if(isFaded){
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Color fadeColor = new Color(0f, 0f, 0f, .66f);
			game.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fadeColor, fadeColor, fadeColor, fadeColor);
		}
		Color backgroundColor = new Color(.227f, .204f, .157f, 1);
		game.shapeRenderer.rect(xOffset, yOffset, width, height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = xOffset;
		int chainYOffset = height + yOffset - chainSize;
		
		while (chainXOffset < xOffset + width){
			if(chainXOffset > xOffset && chainXOffset < xOffset + width - chainSize)
				game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset + height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, yOffset - shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset >= yOffset){
			if(chainYOffset > yOffset && chainYOffset < yOffset + height - chainSize)
				game.batcher.draw(game.sprites.ChainVertical, xOffset, chainYOffset, chainSize + shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainVertical, xOffset + width - chainSize, chainYOffset, chainSize + shadowSize, chainSize);
			chainYOffset -= chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, xOffset, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNE, xOffset + width - chainSize, yOffset + height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, xOffset, yOffset, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSE, xOffset + width - chainSize, yOffset, chainSize, chainSize);
	}
	
	public boolean isTouched(final float x, final float y)
	{
		if ((x >= xOffset) && (x < (xOffset + width)))
		{
			if ((y >= yOffset) && (y < (yOffset + height)))
			{
				return true;
			}
		}
		return false;
	}
}
