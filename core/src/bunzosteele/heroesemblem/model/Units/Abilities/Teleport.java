package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Teleport extends Ability
{
	public Teleport()
	{
		this.displayName = "Teleport";
		this.isActive = true;
		this.isTargeted = true;
		this.isMultiInput = true;
		this.isAction = true;
		this.description = "Teleports any unit to an available space on the battlefield. Can be used once per battle.";
	}
	
	public Teleport(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
		this();
		this.exhausted = exhausted;
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
		if (this.targets.size() == 0)
		{
			for (final Unit unit : this.GetTargetableUnits(state))
			{
				if ((targetTile.x == unit.x) && (targetTile.y == unit.y))
				{
					this.targets.add(unit.id);
				}
			}
			return false;
		} else
		{
			if (!this.GetTargetTiles(state, executor).contains(targetTile))
			{
				this.targets = new ArrayList<Integer>();
				return false;
			} else
			{
				Unit target = null;
				for(Unit unit : state.AllUnits()){
					if (unit.id == this.targets.get(0))
						target = unit;
				}
				if (target != null){
					executor.startAttack();
					target.x = targetTile.x;
					target.y = targetTile.y;
					return true;
				}
				return false;
			}
		}
	}

	@Override
	public List<Unit> GetTargetableUnits(final BattleState state)
	{
		return state.AllUnits();
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		if (this.targets.size() == 0)
		{
			for (final Unit unit : state.AllUnits())
			{
				if(unit.x >=0 && unit.y >= 0)
					targets.add(state.battlefield.get(unit.y).get(unit.x));
			}
		} else
		{
			for (final List<Tile> row : state.battlefield)
			{
				for (final Tile tile : row)
				{
					Unit target = null;
					for(Unit unit : state.AllUnits()){
						if (unit.id == this.targets.get(0))
							target = unit;
					}
					if (target != null && tile.movementCost <= target.movement)
					{
						targets.add(tile);
					}
				}
			}
			for (final Unit unit : state.AllUnits())
			{
				final Tile tile = state.battlefield.get(unit.y).get(unit.x);
				targets.remove(tile);
			}
		}
		return targets;
	}
	
	@Override
	public void PlaySound(float volume){
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("teleport.wav"));
		sound.play(volume);
	}
}
