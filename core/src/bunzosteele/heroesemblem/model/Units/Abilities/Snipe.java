package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Snipe extends Ability
{
	public Snipe()
	{
		this.displayName = "Snipe";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(1f, 0f, 0f, .5f);
		this.isAction = true;
	}
	
	public Snipe(boolean exhausted, boolean canUse, List<Integer> abilityTargets){
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
		if (this.GetTargetTiles(state, executor).contains(targetTile))
		{
			for (final Unit unit : state.AllUnits())
			{
				if ((unit.x == targetTile.x) && (unit.y == targetTile.y))
				{
					executor.startAttack();
					int damage = executor.attack;
					final Random random = new Random();
					final int roll = random.nextInt(101);
					if (roll > 95)
					{
						damage = damage * 3;
					} else if (roll > 60)
					{
						damage = damage * 2;
					}
					unit.dealDamage(damage);
					executor.damageDealt += damage;
					unit.startDamage();
					if(unit.checkDeath()){
						unit.killUnit(executor, state.roundsSurvived);
						if(unit.team == 0)
							state.SaveGraveyard(unit);
					}
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
	
	@Override
	public void PlaySound(float volume){
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("snipe.wav"));
		sound.play(volume);
	}
}
