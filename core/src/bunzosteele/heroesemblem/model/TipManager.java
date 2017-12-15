package bunzosteele.heroesemblem.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitType;

public class TipManager
{
	public static String GetTip() throws IOException{
		return ReadTip();
	}
	
	private static String ReadTip() throws IOException{
		FileHandle file  = Gdx.files.internal(fileName);
		List<String> options = new ArrayList<String>(Arrays.asList(file.readString().split("\r\n")));
		Random random = new Random();
		int roll = random.nextInt(options.size());	
		return options.get(roll);
	}
	
	private static final String fileName = "tips.txt";
}
