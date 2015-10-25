package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class ShopScreen extends ScreenAdapter
{

	HeroesEmblem game;
	ShopState state;
	ShopStatusPanel shopStatus;
	StockWindow stockWindow;
	ShopUnitStatusPanel unitStatus;
	ShopControls shopControls;

	public ShopScreen(final HeroesEmblem game) throws IOException
	{
		this.state = new ShopState(game);
		this.InitializeShopScreen(game);
		game.adsController.hideBannerAd();
	}

	public ShopScreen(final HeroesEmblem game, final ShopState state)
	{
		this.state = state;
		this.InitializeShopScreen(game);
		game.adsController.hideBannerAd();
	}

	public void draw() throws IOException
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.game.batcher.begin();
		this.stockWindow.draw();
		this.shopStatus.draw();
		this.unitStatus.draw();
		this.shopControls.draw();
		this.game.batcher.end();
	}

	private void InitializeShopScreen(final HeroesEmblem game)
	{
		this.game = game;

		int sideWidth = Gdx.graphics.getWidth() / 6;
		int controlHeight = Gdx.graphics.getHeight() / 6;
		int windowWidth = Gdx.graphics.getWidth() - sideWidth;
		int windowHeight = Gdx.graphics.getHeight() - controlHeight;
		final double desiredRatio = 9 / (double) 16;
		double actualRatio = windowHeight / (double) windowWidth;
		while (actualRatio != desiredRatio)
		{
			if (actualRatio > desiredRatio)
			{
				windowHeight--;
				controlHeight++;
				actualRatio = windowHeight / (double) windowWidth;
			} else
			{
				windowWidth--;
				sideWidth++;
				actualRatio = windowHeight / (double) windowWidth;
			}
		}
		this.shopStatus = new ShopStatusPanel(game, this.state, sideWidth, windowHeight, controlHeight);
		this.stockWindow = new StockWindow(game, this.state, windowWidth - sideWidth, windowHeight, sideWidth, controlHeight);
		this.unitStatus = new ShopUnitStatusPanel(game, this.state, sideWidth, windowHeight, windowWidth, controlHeight);
		this.shopControls = new ShopControls(game, this.state, sideWidth, windowWidth - sideWidth, controlHeight);
		MusicManager.PlayShopMusic(this.game.settings.getFloat("musicVolume", .5f));
	}

	@Override
	public void render(final float delta)
	{
		try
		{
			this.update();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			this.draw();
		} catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update() throws IOException
	{
		if (Gdx.input.justTouched())
		{
			if (this.stockWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.stockWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.shopStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.shopStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.unitStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.unitStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.shopControls.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.shopControls.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}
		}
	}
}
