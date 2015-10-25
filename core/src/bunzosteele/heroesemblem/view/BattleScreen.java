package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.AiProcessor;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleScreen extends ScreenAdapter
{
	HeroesEmblem game;
	BattleState state;
	BattleWindow battleWindow;
	BattleUnitStatusPanel unitStatus;
	BattleControls battleControls;
	AiProcessor aiProcessor;
	boolean spoofAiThinking = false;

	public BattleScreen(final HeroesEmblem game, final ShopState shopState) throws IOException
	{
		this.state = new BattleState(shopState);
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
		this.battleWindow = new BattleWindow(game, this.state, windowWidth, windowHeight, controlHeight);
		this.unitStatus = new BattleUnitStatusPanel(game, this.state, sideWidth, windowHeight, windowWidth, controlHeight);
		this.battleControls = new BattleControls(game, this.state, controlHeight, windowWidth, sideWidth);
		this.aiProcessor = new AiProcessor(state);
		MusicManager.PlayBattleMusic(this.game.settings.getFloat("musicVolume", .5f));
		game.adsController.hideBannerAd();
	}

	public void draw() throws IOException
	{
		final GL20 gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.game.batcher.begin();
		this.battleWindow.draw();
		this.unitStatus.draw();
		this.battleControls.drawBackground();
		this.battleControls.draw();
		this.game.batcher.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		this.game.shapeRenderer.begin(ShapeType.Filled);
		this.battleWindow.drawHighlights();
		this.game.shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		this.game.shapeRenderer.begin(ShapeType.Filled);
		this.battleWindow.drawHealthBars();
		this.game.shapeRenderer.end();
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
		this.state.CleanBoard();
		if (this.state.roster.size() == 0)
		{
			this.game.setScreen(new GameOverScreen(this.game, this.state.roundsSurvived, this.state.heroUnit));
		}else if (this.state.enemies.size() == 0)
		{
			this.state.EndBattle();
			this.game.setScreen(new ShopScreen(this.game, new ShopState(this.state)));
		}else if(this.state.currentPlayer > 0){
			if(!spoofAiThinking){
				spoofAiThinking = true;
				Timer.schedule(new Task()
				{
					@Override
					public void run()
					{
						spoofAiThinking = false;
					}
				}, this.game.settings.getFloat("cpuSpeed", .6f), 0, 0);
				aiProcessor.MakeMove(this.game.settings.getFloat("sfxVolume", .5f));
			}
		}else if (this.state.currentPlayer == 0 && Gdx.input.justTouched())
		{
			if (this.battleWindow.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.battleWindow.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.unitStatus.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.unitStatus.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			} else if (this.battleControls.isTouched(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				this.battleControls.processTouch(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			}
		}
	}
}