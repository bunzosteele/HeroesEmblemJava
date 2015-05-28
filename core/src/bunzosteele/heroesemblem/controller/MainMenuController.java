package bunzosteele.heroesemblem.controller;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MainMenuController {
		public List<Button> buttons;
		
		public MainMenuController(){
			buttons = GenerateButtons();
		}
			
		private List<Button> GenerateButtons(){
			Button newGame = new Button();
			List<Button> buttonList = new ArrayList<Button>();
			buttonList.add(newGame);
			return buttonList;
		}
}
