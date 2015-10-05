package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Snipe extends Ability
{
	private static Sound sound = Gdx.audio.newSound(Gdx.files.internal("snipe.wav"));
	
	public Snipe()
	{
		this.displayName = "Snipe";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(1f, 0f, 0f, .5f);
	}

	@Override
	public boolean CanUse(final BattleState state, final Unit originUnit)
	{
		if (originUnit.hasAttacked)
		{
			return false;
		}

		if (this.exhausted)
		{
			return false;
		}

		return true;
	}

	@Override
	public boolean Execute(final BattleState state, Unit executor, final Tile targetTile)
	{
		if (this.GetTargetTiles(state, executor).contains(targetTile))
		{
			for (final Unit unit : state.AllUnits())
			{
				if ((unit.x == targetTile.x) && (unit.y == targetTile.y))
				{
					executor.startAttack();
					Snipe.sound.play();
					unit.dealDamage(executor.attack);
					unit.startDamage();
					unit.checkDeath(executor);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		for (final Unit unit : state.enemies)
		{
			targets.add(state.battlefield.get(unit.y).get(unit.x));
		}
		return targets;
	}
}
