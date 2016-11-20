package bunzosteele.heroesemblem;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class SpriteHolder {
	public Sprite SettingsIcon;
	public Sprite ChainHorizontal;
	public Sprite ChainVertical;
	public Sprite ChainNW;
	public Sprite ChainNE;
	public Sprite ChainSW;
	public Sprite ChainSE;
	public Sprite EndTurnDisabled;
	public Sprite EndTurnEnabled;
	public Sprite EndTurnEmphasized;
	public Sprite ConfirmDisabled;
	public Sprite ConfirmEnabled;
	public Sprite HealthBackdrop;
	public Sprite ExperienceBackdrop;
	public Sprite AttackBackdrop;
	public Sprite AccuracyBackdrop;
	public Sprite DefenseBackdrop;
	public Sprite EvasionBackdrop;
	public Sprite ControlsDivider;
	public Sprite AbilityEnabled;
	public Sprite AbilityEmphasis;
	public Sprite AbilityDisabled;
	public Sprite InfoDisabled;
	public Sprite InfoOpen;
	public Sprite InfoClose;
	public Sprite UndoEnabled;
	public Sprite BlueTile;
	public Sprite GoldTile;
	public Sprite GreenTile;
	public Sprite PurpleTile;
	public Sprite RedTile;
	public Sprite Crosshair;
	public Sprite ProjectionBorder;
	public Sprite ProjectionBackground;
	public Sprite CritBackground;
	public Sprite DamageBackground;
	public Sprite HealthBarBackground;
	public Sprite ShopBackground;
	public Sprite CombatEnabled;
	public Sprite CombatEmphasis;
	public Sprite CombatDisabled;
	public Sprite HireEnabled;
	public Sprite HireDisabled;
	public Sprite PurchaseEnabled;
	public Sprite PurchaseDisabled;
	public Sprite MapEnabled;
	public Sprite MapDisabled;
	public Sprite ShopDivider;
	public Sprite BlueSelect;
	public Sprite RedSelect;
	public Sprite GoldCoin;
	public Sprite RerollDisabled;
	public Sprite RerollEnabled;
	public Sprite StockNameBackdrop;
	public Sprite ShopLabel;
	public Sprite Shopkeeper;

	public SpriteHolder(HeroesEmblem game) throws ReflectionException, IllegalArgumentException, IllegalAccessException {
		java.lang.reflect.Field[] fields = getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			LoadSprite(game, this, fields[i]);
		}
	}

	private static void LoadSprite(HeroesEmblem game, SpriteHolder spriteHolder, java.lang.reflect.Field sprite) throws ReflectionException, IllegalArgumentException, IllegalAccessException {
		sprite.set(spriteHolder, new Sprite(game.textureAtlas.findRegion(sprite.getName())));
	}
}
