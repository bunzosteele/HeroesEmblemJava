package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.controller.MainMenuController;

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

public class MainMenuScreen extends ScreenAdapter{
	HeroesEmblem game;
	OrthographicCamera camera;
	Rectangle newGameBounds;
	Vector3 touchpoint;
	
	public MainMenuScreen(HeroesEmblem game) {
		this.game = game;
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		newGameBounds = new Rectangle(Gdx.graphics.getWidth() /4, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() /2);
		touchpoint = new Vector3();
	}
	
	public void update() throws IOException{
		if (Gdx.input.justTouched()){
			touchpoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			if(newGameBounds.contains(touchpoint.x, touchpoint.y)){
				game.setScreen(new ShopScreen(game));
				return;
			}
		}
	}
	
	public void draw(){
		GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		game.shapeRenderer.begin(ShapeType.Filled);
		game.shapeRenderer.setColor(0,1,0,1);	
		game.shapeRenderer.rect(newGameBounds.x, newGameBounds.y, newGameBounds.width, newGameBounds.height);
		game.shapeRenderer.end();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("LeagueGothic-CondensedRegular.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 144;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		
		game.batcher.begin();
		font.draw(game.batcher, "New Game", newGameBounds.x, newGameBounds.y + 3* newGameBounds.height/4, newGameBounds.width, 1, false);
		game.batcher.end();
		font.dispose();
	}
	
	@Override
	public void render(float delta){
		try {
			update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		draw();
	}
}
