package bunzosteele.heroesemblem.model.Battlefield;

public class Spawn
{
	public int x;
	public int y;
	public boolean isPlayer;

	public Spawn(final int x, final int y, final boolean isPlayer)
	{
		this.x = x;
		this.y = y;
		this.isPlayer = isPlayer;
	}
}
