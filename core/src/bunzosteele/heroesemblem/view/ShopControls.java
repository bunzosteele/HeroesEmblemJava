package bunzosteele.heroesemblem.view;

import java.io.IOException;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class ShopControls
{
	HeroesEmblem game;
	ShopState state;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int buttonWidth;
	int rosterWidth;
	int height;
	public static Sound buySound = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
	public static Sound finalBuySound = Gdx.audio.newSound(Gdx.files.internal("finalbuy.wav"));
	Sprite inactiveButton;
	Sprite activeButton;
	Sprite emphasisButton;
	Sprite backdrop;

	public ShopControls(final HeroesEmblem game, final ShopState state, final int buttonWidth, final int rosterWidth, final int height)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				ShopControls.this.idleFrame++;
				ShopControls.this.attackFrame++;
				if (ShopControls.this.idleFrame > 3)
				{
					ShopControls.this.idleFrame = 1;
				}
				if (ShopControls.this.attackFrame > 2)
				{
					ShopControls.this.attackFrame = 1;
				}
			}
		}, 0, 1 / 3.0f);
		this.xOffset = 0;
		this.yOffset = 0;
		this.buttonWidth = buttonWidth;
		this.rosterWidth = rosterWidth;
		this.height = height;
		final AtlasRegion inactiveRegion = this.game.textureAtlas.findRegion("InactiveButton");
		this.inactiveButton = new Sprite(inactiveRegion);
		final AtlasRegion activeRegion = this.game.textureAtlas.findRegion("ActiveButton");
		this.activeButton = new Sprite(activeRegion);
		final AtlasRegion emphasisRegion = this.game.textureAtlas.findRegion("EmphasisButton");
		this.emphasisButton = new Sprite(emphasisRegion);
		final AtlasRegion backdropRegion = this.game.textureAtlas.findRegion("BackdropBottom");
		this.backdrop = new Sprite(backdropRegion);
	}

	private boolean canPurchaseSelected()
	{
		return (this.state.selected != null) && (this.state.gold >= this.state.selected.cost) && (this.state.roster.size() < 8) && !this.state.roster.contains(this.state.selected);
	}

	private boolean canStartGame()
	{
		return this.state.roster.size() > 0;
	}

	public void draw()
	{
		this.drawBuyBackground();
		this.drawRosterBackground();
		this.drawCompleteBackground();
		this.game.font.getData().setScale(.25f);
		this.drawBuy();
		this.game.font.getData().setScale(.33f);
		this.drawRoster();
		this.game.font.getData().setScale(.25f);
		this.drawComplete();
		this.game.font.getData().setScale(.33f);
	}

	private void drawBuy()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "Buy", this.xOffset, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
	}

	private void drawBuyBackground()
	{
		if (this.canPurchaseSelected())
		{
			this.game.batcher.draw(activeButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}else{
			this.game.batcher.draw(inactiveButton, this.xOffset, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawComplete()
	{
		this.game.font.setColor(Color.WHITE);
		this.game.font.draw(this.game.batcher, "Complete", this.buttonWidth + this.rosterWidth, this.yOffset + ((3 * this.height) / 5), this.buttonWidth, 1, false);
	}

	private void drawCompleteBackground()
	{
		boolean canAffordMoreUnits = false;
		for (final Unit unit : this.state.stock)
		{
			if (unit.cost < this.state.gold)
			{
				canAffordMoreUnits = true;
			}
		}		
		if(this.canStartGame()){
			if (!canAffordMoreUnits || (this.state.roster.size() >= 8))
			{
				this.game.batcher.draw(emphasisButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}else{
				this.game.batcher.draw(activeButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
			}
		}else{
			this.game.batcher.draw(inactiveButton, this.buttonWidth + this.rosterWidth, this.yOffset, this.buttonWidth, this.height);
		}
	}

	private void drawRoster()
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;

		for (final Unit unit : this.state.roster)
		{
			if ((this.state.selected != null) && this.state.selected.isEquivalentTo(unit))
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, "Attack", this.attackFrame);
			} else
			{
				UnitRenderer.DrawUnit(this.game, unit, this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset), (this.height - columnWidth) / 2, columnWidth, "Idle", this.idleFrame);
			}
			unitOffset++;
		}
	}

	public void drawRosterBackground()
	{
		this.game.batcher.draw(backdrop, this.buttonWidth, this.yOffset, this.rosterWidth, this.height);
	}

	public boolean isTouched(final float x, final float y)
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				return true;
			}
		}
		return false;
	}

	private void processBuyTouch() throws IOException
	{
		if (this.canPurchaseSelected())
		{
			this.state.roster.add(this.state.selected);
			if(this.state.roster.size() == 1){
				this.state.heroUnit = this.state.selected;
			}
			String action = "" + this.state.roundsSurvived;
			UnitDto unitDto = new UnitDto();
			unitDto.type = this.state.selected.type.toString();
			unitDto.attack = this.state.selected.attack;
			unitDto.defense = this.state.selected.defense;
			unitDto.evasion = this.state.selected.evasion;
			unitDto.accuracy = this.state.selected.accuracy;
			unitDto.movement = this.state.selected.movement;
			unitDto.maximumHealth = this.state.selected.maximumHealth;
			if(this.state.selected.ability == null){
				unitDto.ability = "None";
			}else{
				unitDto.ability = this.state.selected.ability.displayName;
			}
			Json json = new Json();
			String parsedUnit = json.toJson(unitDto);
			this.game.gameServicesController.RecordAnalyticsEvent("UnitPurchased", action, parsedUnit, this.state.selected.cost);
			this.state.gold -= this.state.selected.cost;
			this.state.selected = null;
			this.state.isInspecting = false;
			this.state.stock = UnitGenerator.GenerateStock(this.state.roster, this.game);
			boolean canBuy = false;
			for(Unit unit : this.state.stock){
				if (unit.cost < this.state.gold)
					canBuy = true;
			}			
			if(canBuy){
				ShopControls.buySound.play(this.game.settings.getFloat("sfxVolume", .5f));
			}else{
				ShopControls.finalBuySound.play(this.game.settings.getFloat("sfxVolume", .5f));
			}
		}
	}

	private void processCompleteTouch() throws IOException
	{
		if (this.canStartGame())
		{
			this.game.setScreen(new BattleScreen(this.game, this.state));
			return;
		}
	}

	private void processRosterTouch(final float x)
	{
		int unitOffset = 0;
		final int columnWidth = this.rosterWidth / 10;
		final int gapWidth = ((2 * this.rosterWidth) / 10) / 9;
		boolean isHit = false;

		for (final Unit unit : this.state.roster)
		{
			final int lowerXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset);
			final int upperXBound = this.xOffset + this.buttonWidth + gapWidth + ((gapWidth * unitOffset) + 1) + (columnWidth * unitOffset) + columnWidth;

			if ((x >= lowerXBound) && (x <= upperXBound))
			{
				isHit = true;
				this.state.selected = this.state.roster.get(unitOffset);
			}
			unitOffset++;
		}
		if (!isHit)
		{
			this.state.selected = null;
			this.state.isInspecting = false;
		}
	}

	public void processTouch(final float x, final float y) throws IOException
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.buttonWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processBuyTouch();
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth)) && (x < (this.xOffset + this.buttonWidth + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processRosterTouch(x);
			}
		}
		if ((x >= (this.xOffset + this.buttonWidth + this.rosterWidth)) && (x < (this.xOffset + (2 * this.buttonWidth) + this.rosterWidth)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				this.processCompleteTouch();
			}
		}
	}
}
