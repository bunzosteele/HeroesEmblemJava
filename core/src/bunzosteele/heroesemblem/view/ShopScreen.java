package bunzosteele.heroesemblem.view;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.controller.MainMenuController;
import bunzosteele.heroesemblem.model.ShopState;

public class ShopScreen extends ScreenAdapter{
	
	HeroesEmblem game;
	OrthographicCamera camera;
	Vector3 touchpoint;
	ShopState state;
	ShopStatusPanel shopStatus;
	StockWindow stockWindow;
	ShopUnitStatusPanel unitStatus;
	ShopControls shopControls;
	
	public ShopScreen(HeroesEmblem game) throws IOException{
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
		state = new ShopState();
		shopStatus = new ShopStatusPanel(game, state);
		stockWindow = new StockWindow(game, state);
		unitStatus = new ShopUnitStatusPanel(game, state);
		shopControls = new ShopControls(game, state);
	}
	
	public ShopScreen(HeroesEmblem game, ShopState state){
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
	}
	
	public void update() throws IOException{
		if (Gdx.input.justTouched()){
			touchpoint.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 0);
			if (stockWindow.isTouched(touchpoint.x, touchpoint.y)){
				stockWindow.processTouch(touchpoint.x, touchpoint.y);
			}else if(shopStatus.isTouched(touchpoint.x, touchpoint.y)){
				shopStatus.processTouch(touchpoint.x, touchpoint.y);
			}else if(unitStatus.isTouched(touchpoint.x, touchpoint.y)){
				unitStatus.processTouch(touchpoint.x, touchpoint.y);
			}else if(shopControls.isTouched(touchpoint.x, touchpoint.y)){
				shopControls.processTouch(touchpoint.x, touchpoint.y);
			}
		}
	}
	
	public void draw() throws IOException{
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		shopStatus.draw();
		stockWindow.draw();
		unitStatus.draw();
		shopControls.draw();
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
