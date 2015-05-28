package bunzosteele.heroesemblem.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.Units.Unit;

public final class UnitRenderer {
	
	public static void DrawUnit(HeroesEmblem game, Unit unit, int x, int y, int scaledSize, String animation, int frame){
		TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("HeroesEmblem.pack"));
		AtlasRegion region = textureAtlas.findRegion(unit.type + "-" + animation + "-" + frame + "-" + unit.team);
		Sprite sprite = new Sprite(region);
		game.batcher.begin();
		game.batcher.draw(sprite, x, y, scaledSize, scaledSize);
		game.batcher.end();
		textureAtlas.dispose();
	}
}
