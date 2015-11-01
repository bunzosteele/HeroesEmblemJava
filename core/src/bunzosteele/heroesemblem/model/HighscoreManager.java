package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

import bunzosteele.heroesemblem.model.Units.Unit;
import bunzosteele.heroesemblem.model.Units.UnitDto;
import bunzosteele.heroesemblem.model.Units.UnitType;

public class HighscoreManager
{
	public static void RecordGame(int roundsSurvived, UnitDto hero){
		HighscoreDto score = HighscoreManager.GetHighscoreDto(roundsSurvived, hero);
		HighscoresDto highscores = GetExistingHighscores();
		List<HighscoreDto> newHighscores = new ArrayList<HighscoreDto>();
		HighscoreDto currentScore = score;
		if(highscores != null){
			for(HighscoreDto highscore : highscores.highscores){
				if(highscore.roundsSurvived > currentScore.roundsSurvived){
					newHighscores.add(highscore);
				}
				else if(highscore.roundsSurvived == currentScore.roundsSurvived){
					if(highscore.heroUnit.level > currentScore.heroUnit.level){
						newHighscores.add(highscore);
					}
					else if(highscore.heroUnit.level == currentScore.heroUnit.level){
						if(highscore.heroUnit.name.compareTo(currentScore.heroUnit.name) < 0){
							newHighscores.add(highscore);
						}else{
							newHighscores.add(currentScore);
							currentScore = highscore;
						}
					}
					else{
						newHighscores.add(currentScore);
						currentScore = highscore;
					}
				}else{
					newHighscores.add(currentScore);
					currentScore = highscore;
				}
			}
		}
		newHighscores.add(currentScore);
		if(newHighscores.size() > 3)
			newHighscores.remove(3);
		
		HighscoresDto newHighscoresDto = new HighscoresDto();
		newHighscoresDto.highscores = newHighscores;
		Json json = new Json();
		HighscoreManager.WriteHighscores(json.toJson(newHighscoresDto));
	}
	
	public static HighscoresDto GetExistingHighscores(){
		String scoresString = ReadHighscores();
		Json json = new Json();
		return json.fromJson(HighscoreManager.HighscoresDto.class, scoresString);
	}
	
	public static void EraseHighscores(){
		WriteHighscores("");
	}
	
	private static void WriteHighscores(String jsonScores){
		FileHandle file = Gdx.files.local(HighscoreManager.fileName);
		file.writeString(Base64Coder.encodeString(jsonScores), false);
	}
	
	private static String ReadHighscores(){
		FileHandle file = Gdx.files.local(HighscoreManager.fileName);
		if(file != null && file.exists()){
			String jsonScores = file.readString();
			if(!jsonScores.isEmpty()){
				return Base64Coder.decodeString(jsonScores);
			}
		}
		return "";
	}	
	
	private static HighscoreDto GetHighscoreDto(int roundsSurvived, UnitDto hero){
		HighscoreDto score = new HighscoreDto();
		score.roundsSurvived = roundsSurvived;
		score.heroUnit = hero;
		return score;
	}
	
	public static class HighscoresDto{
		public List<HighscoreDto> highscores;
	}
	
	public static class HighscoreDto{
		public int roundsSurvived;
		public UnitDto heroUnit;
	}
	
	private static final String fileName = "heroesemblem.sav";
}
