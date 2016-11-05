package bunzosteele.heroesemblem.view;

import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattlePanel
{
	HeroesEmblem game;
	BattleState state;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int smallButtonSize;
	int endTurnX;
	int endTurnY;
	int confirmX;
	int confirmY;
	int chainSize;
	int shadowSize;

	public BattlePanel(final HeroesEmblem game, final BattleState state, final int width, final int height, final int xOffset, final int yOffset)
	{
		this.game = game;
		this.state = state;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.smallButtonSize = width / 3;
		this.chainSize = 20;
		this.shadowSize = chainSize / 4; 
		this.endTurnX = xOffset + width - smallButtonSize - (chainSize - shadowSize) * 2;
		this.endTurnY = (chainSize * 2) - shadowSize;
		this.confirmX = xOffset + (chainSize - shadowSize) * 2;
		this.confirmY = (chainSize * 2) - shadowSize;
	}

	public void draw() throws IOException
	{
		drawBorder();
		drawUnitStats();
		drawButtons();
	}

	public void drawBackground()
	{
		Color backgroundColor = new Color(.227f, .204f, .157f, 1);
		this.game.shapeRenderer.rect(this.xOffset, this.yOffset, this.width, this.height, backgroundColor, backgroundColor, backgroundColor, backgroundColor);
	}
	
	public void drawBorder(){
		int chainXOffset = 0;
		int chainYOffset = 0;
		while (chainXOffset < width){
			this.game.batcher.draw(this.game.sprites.horizontalChain, this.xOffset + chainXOffset, Gdx.graphics.getHeight() - chainSize, chainSize, chainSize);
			this.game.batcher.draw(this.game.sprites.horizontalChain, this.xOffset + chainXOffset, -shadowSize, chainSize, chainSize);
			chainXOffset += chainSize;
		}
		while (chainYOffset < height){
			this.game.batcher.draw(this.game.sprites.verticalChain, this.xOffset, chainYOffset, chainSize, chainSize);
			this.game.batcher.draw(this.game.sprites.verticalChain, this.xOffset + this.width - (chainSize - shadowSize), chainYOffset, chainSize, chainSize);
			chainYOffset += chainSize;
		}
		
		this.game.batcher.draw(this.game.sprites.chainNW, this.xOffset, Gdx.graphics.getHeight() - (chainSize), chainSize, chainSize);
		this.game.batcher.draw(this.game.sprites.chainNE, this.xOffset + this.width - chainSize, Gdx.graphics.getHeight() - (chainSize), chainSize -1, chainSize-1);
		this.game.batcher.draw(this.game.sprites.chainSW, this.xOffset -1, 0, chainSize -1, chainSize -1);
		this.game.batcher.draw(this.game.sprites.chainSE, this.xOffset + this.width - chainSize, 0, chainSize + 1, chainSize - 1);	
	}
	
	public void drawUnitStats() throws IOException{
		if(this.state.selected != null){
			game.font.setColor(Color.BLACK);
			game.font.getData().setScale(.1f);
			int portraitSize = (int) Math.floor(smallButtonSize * .8);
			final AtlasRegion portraitRegion = game.textureAtlas.findRegion("Portrait" + this.state.selected.type + this.state.selected.team);
			this.game.batcher.draw(new Sprite(portraitRegion), this.xOffset + this.chainSize, this.height - this.chainSize - portraitSize, portraitSize, portraitSize);
	
			int bannerWidth = smallButtonSize / 3;
			final AtlasRegion bannerRegion = game.textureAtlas.findRegion("Banner" + this.state.selected.team);
			this.game.batcher.draw(new Sprite(bannerRegion), this.xOffset + this.chainSize, this.height - (this.chainSize) - (bannerWidth * 3 / 2), bannerWidth, bannerWidth * 3 / 2);
			
			final AtlasRegion nameBackdropRegion = game.textureAtlas.findRegion("NameBackdrop" + this.state.selected.team);
			int nameBackdropWidth = width - chainSize * 2 - portraitSize;
			this.game.batcher.draw(new Sprite(nameBackdropRegion), this.xOffset + (this.chainSize + portraitSize), this.height - (this.chainSize) - portraitSize, nameBackdropWidth, portraitSize);
			game.font.draw(game.batcher, state.selected.name, this.xOffset + (this.chainSize + portraitSize), this.height - (this.chainSize) - game.font.getData().lineHeight / 2, nameBackdropWidth - (nameBackdropWidth / 10), 1, false);
			
			int statBackdropWidth = (int) Math.floor(portraitSize * 2.83);
			final AtlasRegion healthBackdropRegion = game.textureAtlas.findRegion("HealthBackdrop");
			final AtlasRegion experienceBackdropRegion = game.textureAtlas.findRegion("ExperienceBackdrop");
			final AtlasRegion attackBackdropRegion = game.textureAtlas.findRegion("AttackBackdrop");
			final AtlasRegion accuracyBackdropRegion = game.textureAtlas.findRegion("AccuracyBackdrop");
			final AtlasRegion defenseBackdropRegion = game.textureAtlas.findRegion("DefenseBackdrop");
			final AtlasRegion evasionBackdropRegion = game.textureAtlas.findRegion("EvasionBackdrop");
			
			this.game.batcher.draw(new Sprite(healthBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 2, statBackdropWidth, portraitSize);
			game.font.draw(game.batcher, state.selected.currentHealth + "/" + state.selected.maximumHealth, this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize - game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
			this.game.batcher.draw(new Sprite(experienceBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 3, statBackdropWidth, portraitSize);
			game.font.draw(game.batcher, "LVL." + state.selected.level, this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize * 2 - game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
			if (state.selected.team == 0 || state.perksPurchased >= 2) {
				game.font.getData().setScale(.15f);
				this.game.batcher.draw(new Sprite(attackBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 4, statBackdropWidth, portraitSize);
				game.font.draw(game.batcher, "" + state.selected.attack, this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize * 7 / 2 + game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(new Sprite(accuracyBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 5, statBackdropWidth, portraitSize);
				game.font.draw(game.batcher, state.selected.accuracy + "%", this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize * 9 / 2 + game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(new Sprite(defenseBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 6, statBackdropWidth, portraitSize);
				UnitRenderer.SetDefenseFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, "" + (state.selected.defense + state.battlefield.get(state.selected.y).get(state.selected.x).defenseModifier), this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize * 11 / 2 + game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
				this.game.batcher.draw(new Sprite(evasionBackdropRegion), this.xOffset + (width - statBackdropWidth) / 2, this.height - (this.chainSize) - portraitSize * 7, statBackdropWidth, portraitSize);
				UnitRenderer.SetEvasionFont(state.selected, null, state.battlefield, game.font);
				game.font.draw(game.batcher, (state.selected.evasion + state.battlefield.get(state.selected.y).get(state.selected.x).evasionModifier) + "%", this.xOffset + (width - statBackdropWidth) / 2 + portraitSize, this.height - (this.chainSize) - portraitSize * 13 / 2 + game.font.getData().lineHeight / 2, statBackdropWidth - (statBackdropWidth * 2 / 29) - portraitSize - (nameBackdropWidth / 10), 1, false);
			}
			
			game.font.getData().setScale(.33f);
		}
	}
	
	public void drawButtons(){
		if(this.state.currentPlayer == 0){
			if (!this.hasActions())
			{
				this.game.batcher.draw(this.game.sprites.endTurnEmphasized, this.endTurnX, this.endTurnY, smallButtonSize, smallButtonSize);
			}else{
				this.game.batcher.draw(this.game.sprites.endTurnEnabled, this.endTurnX, this.endTurnY, smallButtonSize, smallButtonSize);
			}
		}else{
			this.game.batcher.draw(this.game.sprites.endTurnDisabled, this.endTurnX, this.endTurnY, smallButtonSize, smallButtonSize);
		}
		
		this.game.batcher.draw(this.game.sprites.confirmDisabled, this.confirmX, this.confirmY, smallButtonSize, smallButtonSize);
	}
	
	private boolean hasActions()
	{
		for (final Unit unit : this.state.roster)
		{
			if (!this.state.IsTapped(unit))
			{
				return true;
			}
		}
		return false;
	}


	public boolean isTouched(final float x, final float y)
	{
		if ((x >= this.xOffset) && (x < (this.xOffset + this.width)))
		{
			if ((y >= this.yOffset) && (y < (this.yOffset + this.height)))
			{
				return true;
			}
		}
		return false;
	}

	public void processTouch(final float x, final float y)
	{
		int clickedX = Gdx.input.getX();
		int clickedY = Gdx.graphics.getHeight() - Gdx.input.getY();
		if((clickedX > endTurnX && clickedX < endTurnX + smallButtonSize) && (clickedY > endTurnY && clickedY < endTurnY + smallButtonSize)){
			processEndTouch();
		}else{
			this.state.selected = null;
			this.state.isMoving = false;
			this.state.isUsingAbility = false;
			this.state.isAttacking = false;
			this.state.isPreviewingAttack = false;
			this.state.isPreviewingAbility = false;
		}
	}
	
	public void processEndTouch()
	{
		this.state.EndTurn();
	}
}
