package bunzosteele.heroesemblem.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import bunzosteele.heroesemblem.HeroesEmblem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MenuScreen extends ScreenAdapter {
	
	HeroesEmblem game;
	Sprite backgroundSprite;
	Sprite altBackgroundSprite;
	Map<Integer, Integer> alternateCoordinates;
	int idleFrame = 0;
	int attackFrame = 0;
	
	public MenuScreen(HeroesEmblem game){
		this.game = game;
		final AtlasRegion backgroundRegion = this.game.textureAtlas.findRegion("Flooring-1");
		final AtlasRegion altBackgroundRegion = this.game.textureAtlas.findRegion("Flooring-2");
		backgroundSprite = new Sprite(backgroundRegion);
		altBackgroundSprite = new Sprite(altBackgroundRegion);
		alternateCoordinates = new HashMap<Integer, Integer>();
		Random random = new Random();
		for(int i = 0; i < 19; i++){
			for(int j = 0; j < 11; j++){
				int roll = random.nextInt(20);
				if(roll == 0){
					alternateCoordinates.put(i, j);
				}
			}
		}
		
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
	
	public void drawBackground(){
		for(int i = 0; i < 20; i++){
			for(int j = 0; j < 12; j++){
				if(alternateCoordinates.containsKey(i) && alternateCoordinates.get(i) == j){
					this.game.batcher.draw(altBackgroundSprite, (Gdx.graphics.getWidth() / 19) * i, (Gdx.graphics.getHeight() / 11) * j, (Gdx.graphics.getWidth() / 19), (Gdx.graphics.getHeight() / 11));				
				}else{
					this.game.batcher.draw(backgroundSprite, (Gdx.graphics.getWidth() / 19) * i, (Gdx.graphics.getHeight() / 11) * j, (Gdx.graphics.getWidth() / 19), (Gdx.graphics.getHeight() / 11));
				}
			}
		}
	}
}
