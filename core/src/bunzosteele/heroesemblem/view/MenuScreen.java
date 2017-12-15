package bunzosteele.heroesemblem.view;

import java.util.Map;

import bunzosteele.heroesemblem.HeroesEmblem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MenuScreen extends ScreenAdapter {
	
	HeroesEmblem game;
	Map<Integer, Integer> alternateCoordinates;
	int idleFrame = 0;
	int attackFrame = 0;
	int width;
	int height;
	int tileSize;
	int chainSize;
	int shadowSize;
	
	public MenuScreen(HeroesEmblem game){
		this.game = game;
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		this.tileSize = Gdx.graphics.getWidth() / 16;
		this.chainSize = tileSize / 5;
		this.shadowSize = chainSize / 3;
		
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				++idleFrame;
				++attackFrame;
				if (attackFrame > 1){
					attackFrame = 0;
				}
				if (idleFrame > 2)
				{
					idleFrame = 0;
				}
			}
		}, 0, 1 / 3f);
	}
	
	public void setupDraw(){
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.game.font.getData().setScale(.33f);
	}
	
	public void drawBackground()
	{
		Color backgroundColor = new Color(.227f, .204f, .157f, 1);
		game.shapeRenderer.rect(0, 0, width, height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		int chainYOffset = height  - chainSize;
		
		while (chainXOffset < width){
			if(chainXOffset > 0 && chainXOffset < width - chainSize)
				game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, height - chainSize - shadowSize, chainSize, chainSize + shadowSize);
			game.batcher.draw(game.sprites.ChainHorizontal, chainXOffset, -shadowSize, chainSize, chainSize + shadowSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset >= 0){
			if(chainYOffset > 0 && chainYOffset < height - chainSize)
				game.batcher.draw(game.sprites.ChainVertical, 0, chainYOffset, chainSize + shadowSize, chainSize);
			game.batcher.draw(game.sprites.ChainVertical, width - chainSize, chainYOffset, chainSize + shadowSize, chainSize);
			chainYOffset -= chainSize;
		}
		
		game.batcher.draw(game.sprites.ChainNW, 0, height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainNE, width - chainSize, height - chainSize, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSW, 0, 0, chainSize, chainSize);
		game.batcher.draw(game.sprites.ChainSE, width - chainSize, 0, chainSize, chainSize);
	}
}
