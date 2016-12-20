package bunzosteele.heroesemblem;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class HeroesGestureListener implements GestureListener{
	
	private HeroesEmblem game;
	
	public HeroesGestureListener(HeroesEmblem game){
		super();
		this.game = game;	
	}

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        if(game.battleState != null){
        	game.battleState.isLongPressed = true;
        	return false;
        }
        
        if(game.shopState != null){
        	game.shopState.isLongPressed = true;
        	return true;
        }
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

       return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

       return false;
    }
}