package bunzosteele.heroesemblem.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.CombatHelper;
import bunzosteele.heroesemblem.model.MovementHelper;
import bunzosteele.heroesemblem.model.BattleState.Move;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleWindow
{
	HeroesEmblem game;
	BattleState state;
	Texture img;
	int idleFrame = 1;
	int attackFrame = 1;
	int xOffset;
	int yOffset;
	int width;
	int height;
	int tileWidth;
	int tileHeight;

	public BattleWindow(final HeroesEmblem game, final BattleState state, final int width, final int height, final int yOffset)
	{
		this.game = game;
		this.state = state;
		Timer.schedule(new Task()
		{
			@Override
			public void run()
			{
				BattleWindow.this.idleFrame++;
				if (BattleWindow.this.idleFrame > 3)
				{
					BattleWindow.this.idleFrame = 1;
				}
			}
		}, 0, 1 / 3f);
		this.xOffset = 0;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
		this.tileWidth = width / 16;
		this.tileHeight = height / 9;
	}

	public void draw()
	{
		this.drawBattlefield();
		this.drawUnits();
	}

	private void drawBattlefield()
	{
		int rowOffset = 1;
		for (final List<Tile> row : this.state.battlefield)
		{
			int tileOffset = 0;
			for (final Tile tile : row)
			{
				final AtlasRegion tileRegion = this.game.textureAtlas.findRegion(tile.type);
				final Sprite tileSprite = new Sprite(tileRegion);
				this.game.batcher.draw(tileSprite, this.xOffset + (this.tileWidth * tileOffset), Gdx.graphics.getHeight() - (this.tileHeight * rowOffset), this.tileWidth, this.tileHeight);
				tileOffset++;
			}
			rowOffset++;
		}
	}

	private void drawHealthBar(final Unit unit)
	{
		final float healthPercent = unit.currentHealth / (float) unit.maximumHealth;
		if ((healthPercent > 0) && (healthPercent < 1))
		{
			Color color;
			if (healthPercent > .7)
			{
				color = new Color(0f, 1f, 0f, 1f);
			} else if (healthPercent > .3)
			{
				color = new Color(1f, 1f, 0f, 1f);
			} else
			{
				color = new Color(1f, 0f, 0f, 1f);
			}

			this.game.shapeRenderer.setColor(color);
			this.game.shapeRenderer.rect((unit.x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (unit.y + 1)), this.tileWidth * healthPercent, this.tileHeight / 10);
		}
	}

	public void drawHealthBars()
	{
		for (final Unit unit : this.state.AllUnits())
		{
			this.drawHealthBar(unit);
		}
	}

	private void drawHighlight(final int x, final int y, final Color color)
	{
		this.game.shapeRenderer.setColor(color);
		this.game.shapeRenderer.rect((x * this.tileWidth) + this.xOffset, Gdx.graphics.getHeight() - (this.tileHeight * (y + 1)), this.tileWidth, this.tileHeight);
	}

	public void drawHighlights()
	{
		if (this.state.isMoving)
		{
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state);
			final Color color = new Color(.2f, .2f, .7f, .5f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if (this.state.isAttacking)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected);
			final Color color = new Color(.7f, .2f, .2f, .5f);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, color);
			}
		}
		if (this.state.isUsingAbility)
		{
			final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
			for (final Tile tile : options)
			{
				this.drawHighlight(tile.x, tile.y, this.state.selected.ability.abilityColor);
			}
		}
		if(this.state.selected != null){
			final Color color = new Color(.35f, .35f, .55f, .3f);
			this.drawHighlight(this.state.selected.x, this.state.selected.y, color);
		}
	}

	private void drawUnits()
	{
		for (final Unit unit : this.state.AllUnits())
		{
			if (unit.isAttacking)
			{
				UnitRenderer.DrawUnit(this.game, unit, unit.x * this.tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight), this.tileWidth, "Attack", unit.attackFrame);
			} else
			{
				UnitRenderer.DrawUnit(this.game, unit, unit.x * this.tileWidth, Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight), this.tileWidth, "Idle", this.idleFrame, this.state.IsTapped(unit));
			}
		}
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
		if ((this.state.selected != null) && this.state.isAttacking)
		{
			final HashSet<Tile> options = CombatHelper.GetAttackOptions(this.state, this.state.selected);
			for (final Tile tile : options)
			{
				if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
					{
						for (final Unit enemy : this.state.enemies)
						{
							if ((enemy.x == tile.x) && (enemy.y == tile.y))
							{
								this.state.selected.startAttack();
								if (CombatHelper.Attack(this.state.selected, enemy, this.state.battlefield))
								{
									if(enemy.currentHealth >= 1){
										Unit.hitSound.play(this.game.settings.getFloat("sfxVolume", .5f));
									}else{
										Unit.deathSound.play(this.game.settings.getFloat("sfxVolume", .5f));
									}
									enemy.startDamage();
									enemy.checkDeath(this.state.selected);
								} else
								{
									Unit.missSound.play(this.game.settings.getFloat("sfxVolume", .5f));
									enemy.startMiss();
								}

								this.state.isAttacking = false;
								this.state.selected.hasAttacked = true;
								this.state.ClearUndos();
								this.state.selected = null;
								return;
							}
						}
					}
				}
			}
		}

		if ((this.state.selected != null) && this.state.isUsingAbility)
		{
			final HashSet<Tile> options = this.state.selected.ability.GetTargetTiles(this.state, this.state.selected);
			for (final Tile tile : options)
			{
				if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
					{
						if (this.state.selected.ability.Execute(this.state, this.state.selected, tile))
						{
							this.state.selected.ability.PlaySound(this.game.settings.getFloat("sfxVolume", .5f));
							this.state.isUsingAbility = false;
							this.state.selected.hasAttacked = true;
							this.state.selected.ability.exhausted = true;
							this.state.ClearUndos();
							this.state.selected = null;			
							return;
						} else
						{
							if (!this.state.selected.ability.isMultiInput || (this.state.selected.ability == null))
							{
								this.state.isUsingAbility = false;
								this.state.selected = null;
							} else
							{
								return;
							}
						}
					}
				}
			}
		}

		for (final Unit unit : this.state.AllUnits())
		{
			if (((unit.x * this.tileWidth) < x) && (x <= ((unit.x * this.tileWidth) + this.tileWidth)))
			{
				if (((Gdx.graphics.getHeight() - ((unit.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((unit.y) * this.tileHeight))))
				{
					if ((this.state.selected != null) && (this.state.selected.ability != null) && (this.state.selected.ability.targets.size() > 0) && !this.state.selected.ability.areTargetsPersistent)
					{
						this.state.selected.ability.targets = new ArrayList<Unit>();
					}
					this.state.selected = unit;
					this.state.isMoving = false;
					this.state.isAttacking = false;
					this.state.isUsingAbility = false;
					return;
				}
			}
		}

		if ((this.state.selected != null) && this.state.isMoving)
		{
			final HashSet<Tile> options = MovementHelper.GetMovementOptions(this.state);
			for (final Tile tile : options)
			{
				if (((tile.x * this.tileWidth) < x) && (x <= ((tile.x * this.tileWidth) + this.tileWidth)))
				{
					if (((Gdx.graphics.getHeight() - ((tile.y + 1) * this.tileHeight)) < y) && (y <= (Gdx.graphics.getHeight() - ((tile.y) * this.tileHeight))))
					{
						this.state.selected.distanceMoved = (Math.abs(this.state.selected.x - tile.x) + Math.abs(this.state.selected.y - tile.y));
						Move oldLocation = state.new Move();
						oldLocation.oldX = state.selected.x;
						oldLocation.oldY = state.selected.y;
						oldLocation.unit = this.state.selected;
						this.state.selected.x = tile.x;
						this.state.selected.y = tile.y;
						this.state.isMoving = false;
						this.state.selected.hasMoved = true;
						this.state.undos.push(oldLocation);
						return;
					}
				}
			}
		}

		if ((this.state.selected != null) && (this.state.selected.ability != null) && (this.state.selected.ability.targets.size() > 0) && !this.state.selected.ability.areTargetsPersistent)
		{
			this.state.selected.ability.targets = new ArrayList<Unit>();
		}

		this.state.selected = null;
		this.state.isAttacking = false;
		this.state.isMoving = false;
		this.state.isUsingAbility = false;
	}
}
