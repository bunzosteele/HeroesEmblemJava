package bunzosteele.heroesemblem.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MainMenuController
{
	public List<Button> buttons;

	public MainMenuController()
	{
		this.buttons = this.GenerateButtons();
	}

	private List<Button> GenerateButtons()
	{
		final Button newGame = new Button();
		final List<Button> buttonList = new ArrayList<Button>();
		buttonList.add(newGame);
		return buttonList;
	}
}
