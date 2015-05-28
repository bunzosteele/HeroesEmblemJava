package bunzosteele.heroesemblem;

import bunzosteele.heroesemblem.view.MainMenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class HeroesEmblem extends Game {
	
	public SpriteBatch batcher;
	public ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		batcher = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}
