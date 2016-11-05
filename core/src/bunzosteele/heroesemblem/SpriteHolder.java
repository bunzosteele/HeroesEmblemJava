package bunzosteele.heroesemblem;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class SpriteHolder {
	
	public Sprite settingsIcon;
	public Sprite horizontalChain;
	public Sprite verticalChain;
	public Sprite chainNW;
	public Sprite chainNE;
	public Sprite chainSW;
	public Sprite chainSE;
	public Sprite endTurnDisabled;
	public Sprite endTurnEnabled;
	public Sprite endTurnEmphasized;
	public Sprite confirmDisabled;
	public Sprite confirmEnabled;
	
	public SpriteHolder(HeroesEmblem game){
		final AtlasRegion settingsRegion = game.textureAtlas.findRegion("settingsIcon");
		this.settingsIcon = new Sprite(settingsRegion);
		final AtlasRegion chainHorizontalRegion = game.textureAtlas.findRegion("Chain-Horizontal");
		this.horizontalChain = new Sprite(chainHorizontalRegion);
		final AtlasRegion chainVerticalRegion = game.textureAtlas.findRegion("Chain-Vertical");
		this.verticalChain = new Sprite(chainVerticalRegion);
		final AtlasRegion chainNWRegion = game.textureAtlas.findRegion("Chain-NW");
		this.chainNW = new Sprite(chainNWRegion);
		final AtlasRegion chainNERegion = game.textureAtlas.findRegion("Chain-NE");
		this.chainNE = new Sprite(chainNERegion);
		final AtlasRegion chainSWRegion = game.textureAtlas.findRegion("Chain-SW");
		this.chainSW = new Sprite(chainSWRegion);
		final AtlasRegion chainSERegion = game.textureAtlas.findRegion("Chain-SE");
		this.chainSE = new Sprite(chainSERegion);
		final AtlasRegion endTurnDisabledRegion = game.textureAtlas.findRegion("EndTurnDisabled");
		this.endTurnDisabled = new Sprite(endTurnDisabledRegion);
		final AtlasRegion endTurnEnabledRegion = game.textureAtlas.findRegion("EndTurnEnabled");
		this.endTurnEnabled = new Sprite(endTurnEnabledRegion);
		final AtlasRegion endTurnEmphasizedRegion = game.textureAtlas.findRegion("EndTurnEmphasized");
		this.endTurnEmphasized = new Sprite(endTurnEmphasizedRegion);
		final AtlasRegion confirmDisabledRegion = game.textureAtlas.findRegion("ConfirmDisabled");
		this.confirmDisabled = new Sprite(confirmDisabledRegion);
		final AtlasRegion confirmEnabledRegion = game.textureAtlas.findRegion("ConfirmEnabled");
		this.confirmEnabled = new Sprite(confirmEnabledRegion);
	}
}
