package bunzosteele.heroesemblem.view;


import java.io.IOException;
import java.util.List;

import bunzosteele.heroesemblem.HeroesEmblem;
import bunzosteele.heroesemblem.model.BattleState;
import bunzosteele.heroesemblem.model.HighscoreManager;
import bunzosteele.heroesemblem.model.MusicManager;
import bunzosteele.heroesemblem.model.ShopState;
import bunzosteele.heroesemblem.model.Units.LocationDto;
import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class HelpPanel extends PopupPanel
{	
	String title = "";
	String text = "";
	boolean isVisible = false;
		
	public HelpPanel(final HeroesEmblem game, int width, int height, int xOffset, int yOffset)
	{
		super(game, width, height, xOffset, yOffset);
	}
	
	public void setWidth(int width){
		this.width = width;
		this.xOffset = (Gdx.graphics.getWidth() - width) / 2;
	}
	
	public void setHeight(int height){
		this.height = height;
		this.yOffset = (Gdx.graphics.getHeight() - height) / 2;
	}

	public void drawBackground(){
		if(isVisible)
			super.drawBackground(false);
	}
	
	public void draw() throws IOException{
		if(isVisible){
			super.drawBorder();
			game.font.setColor(Color.BLACK);
			if(title.length() > 0){
				game.font.getData().setScale(.5f);
				game.font.draw(game.batcher, title, xOffset + chainSize + shadowSize, yOffset + height - game.font.getLineHeight() / 2, width - chainSize * 2 - shadowSize * 2, 1, true);
				game.font.getData().setScale(.33f);
				game.font.draw(game.batcher, text, xOffset + chainSize + shadowSize, yOffset + game.font.getLineHeight() * 7 / 2, width - chainSize * 2 - shadowSize * 2, 1, true);
			}else{
				game.font.getData().setScale(.33f);
				game.font.draw(game.batcher, text, xOffset + chainSize + shadowSize, yOffset + height / 2  + game.font.getLineHeight() / 2, width - chainSize * 2 - shadowSize * 2, 1, true);
			}
		}
	}
}
