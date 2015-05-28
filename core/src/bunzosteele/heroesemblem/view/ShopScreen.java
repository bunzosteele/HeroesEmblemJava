package bunzosteele.heroesemblem.view;

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
	UnitStatusPanel unitStatus;
	ShopControls shopControls;
	
	public ShopScreen(HeroesEmblem game){
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
		state = new ShopState();
		shopStatus = new ShopStatusPanel(game, state);
		stockWindow = new StockWindow(game, state);
		unitStatus = new UnitStatusPanel(game);
		shopControls = new ShopControls(game, state);
	}
	
	public ShopScreen(HeroesEmblem game, ShopState state){
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		touchpoint = new Vector3();
	}
	
	public void update(){

	}
	
	public void draw(){
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		shopStatus.draw();
		stockWindow.draw();
	}
	
	@Override
	public void render(float delta){		
		update();
		draw();
	}
}
