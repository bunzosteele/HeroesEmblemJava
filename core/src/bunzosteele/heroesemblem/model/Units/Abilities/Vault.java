package bunzosteele.heroesemblem.model.Units.Abilities;

import java.util.HashSet;

import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.Battlefield.Tile;
import bunzosteele.heroesemblem.model.Units.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;

public class Vault extends Ability
{
	public Vault()
	{
		this.displayName = "Vault";
		this.isActive = true;
		this.isTargeted = true;
		this.abilityColor = new Color(0f, 0f, 1f, .5f);
	}

	@Override
	public boolean CanUse(final BattleState state, final Unit originUnit)
	{
		if (originUnit.hasAttacked)
		{
			return false;
		}

		return this.GetTargetTiles(state, originUnit).size() > 0;
	}

	@Override
	public boolean Execute(final BattleState state, Unit executor, final Tile targetTile)
	{
		if (this.GetTargetTiles(state, executor).contains(targetTile))
		{
			executor.x = targetTile.x;
			executor.y = targetTile.y;
			return true;
		}
		return false;
	}

	@Override
	public HashSet<Tile> GetTargetTiles(final BattleState state, final Unit originUnit)
	{
		final HashSet<Tile> targets = new HashSet<Tile>();
		for (int xOffset = -2; xOffset <= 2; xOffset++)
		{
			for (int yOffset = -2; yOffset <= 2; yOffset++)
			{
				if (this.isValidTarget(originUnit.x + xOffset, originUnit.y + yOffset, state) && ((Math.abs(xOffset) == 2) || (Math.abs(yOffset) == 2)))
				{
					targets.add(state.battlefield.get(originUnit.y + yOffset).get(originUnit.x + xOffset));
				}
			}
		}
		return targets;
	}

	private boolean isValidTarget(final int x, final int y, final BattleState state)
	{
		return this.isInBounds(x, y, state.battlefield) && this.isEmpty(x, y, state.AllUnits()) && this.isValidTerrain(x, y, state);
	}
	
	@Override
	public void PlaySound(float volume){
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("vault.wav"));
		sound.play(volume);
	}
}
