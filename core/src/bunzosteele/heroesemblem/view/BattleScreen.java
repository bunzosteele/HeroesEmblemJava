package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	UnitStatusPanel unitStatus;
	BattleControls battleControls;
	
	public BattleScreen(HeroesEmblem game, ShopState shopState) throws IOException{
		state = new BattleState(shopState);
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
		battleWindow = new BattleWindow(game, state);
		unitStatus = new UnitStatusPanel(game, state);
		battleControls = new BattleControls(game, state);
	}
	
	public void update() throws IOException{
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
		battleWindow.draw();
		unitStatus.draw();
		battleControls.draw();
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