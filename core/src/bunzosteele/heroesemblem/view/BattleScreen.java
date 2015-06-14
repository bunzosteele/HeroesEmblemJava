package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.ShopState;

public class BattleScreen extends ScreenAdapter{
	
	HeroesEmblem game;
	OrthographicCamera camera;
	Vector3 touchpoint;
	BattleState state;
	BattleWindow battleWindow;
	BattleUnitStatusPanel unitStatus;
	BattleControls battleControls;
	
	public BattleScreen(HeroesEmblem game, ShopState shopState) throws IOException{
		state = new BattleState(shopState);
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
		int sideWidth = Gdx.graphics.getWidth() / 6;
		int controlHeight =  Gdx.graphics.getHeight() / 6;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth;
		int windowHeight = Gdx.graphics.getHeight() - controlHeight;
		double desiredRatio = 9 /(double)16;
		double actualRatio =  windowHeight / (double) windowWidth;		
		while(actualRatio != desiredRatio){
			if(actualRatio > desiredRatio){
				windowHeight--;
				controlHeight++;
				actualRatio =  windowHeight / (double) windowWidth;
			}else{
				windowWidth--;
				sideWidth++;
				actualRatio =  windowHeight / (double) windowWidth;
			}
		}		
		battleWindow = new BattleWindow(game, state, windowWidth, windowHeight, controlHeight);
		unitStatus = new BattleUnitStatusPanel(game, state, sideWidth, windowHeight, windowWidth, controlHeight);
		battleControls = new BattleControls(game, state, controlHeight, windowWidth, sideWidth);
	}
	
	public void update() throws IOException{
		if(state.enemies.size() == 0){
			state.EndBattle();
			game.setScreen(new ShopScreen(game, new ShopState(state)));
		}
		if (Gdx.input.justTouched()){
			touchpoint.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
			if (battleWindow.isTouched(touchpoint.x, touchpoint.y)){
				battleWindow.processTouch(touchpoint.x, touchpoint.y);
			}else if(unitStatus.isTouched(touchpoint.x, touchpoint.y)){
				unitStatus.processTouch(touchpoint.x, touchpoint.y);
			}else if(battleControls.isTouched(touchpoint.x, touchpoint.y)){
				battleControls.processTouch(touchpoint.x, touchpoint.y);
			}
		}
	}
	
	public void draw() throws IOException{
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			camera.update();
			state.CleanBoard();
			
			game.shapeRenderer.begin(ShapeType.Filled);
			unitStatus.drawBackground();
			battleControls.drawBackground();
			game.shapeRenderer.end();

			game.batcher.begin();
			battleWindow.draw();
			unitStatus.draw();
			battleControls.draw();
			game.batcher.end();
			
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			game.shapeRenderer.begin(ShapeType.Filled);
			battleWindow.drawHighlights();
			game.shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			
			game.shapeRenderer.begin(ShapeType.Filled);
			battleWindow.drawHealthBars();
			game.shapeRenderer.end();

	}
	
	@Override
	public void render(float delta){		
		try {
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			draw();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}