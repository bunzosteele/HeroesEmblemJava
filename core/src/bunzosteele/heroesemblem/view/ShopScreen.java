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
		state = new ShopState();
		InitializeShopScreen(game);
	}
	
	public ShopScreen(HeroesEmblem game, ShopState state){
		this.state = state;
		InitializeShopScreen(game);
	}
	
	private void InitializeShopScreen(HeroesEmblem game){
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
		shopStatus = new ShopStatusPanel(game, state, sideWidth, windowHeight, controlHeight);
		stockWindow = new StockWindow(game, state, windowWidth - sideWidth, windowHeight, sideWidth, controlHeight);
		unitStatus = new ShopUnitStatusPanel(game, state, sideWidth, windowHeight, windowWidth, controlHeight);
		shopControls = new ShopControls(game, state, sideWidth, windowWidth - sideWidth, controlHeight);
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
		
		game.shapeRenderer.begin(ShapeType.Filled);
		shopStatus.drawBackground();
		stockWindow.drawBackground();
		unitStatus.drawBackground();
		shopControls.drawBackground();
		game.shapeRenderer.end();

		game.batcher.begin();
		shopStatus.draw();
		stockWindow.draw();
		unitStatus.draw();
		shopControls.draw();
		game.batcher.end();
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
