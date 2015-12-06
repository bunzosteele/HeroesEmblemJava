package bunzosteele.heroesemblem.model;

import java.util.ArrayList;
import java.util.Collections;
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
	public static void RecordGame(int score, UnitDto hero){
		HighscoreDto newHighscore = HighscoreManager.GetHighscoreDto(score, hero);
		HighscoresDto highscores = GetExistingHighscores();
		List<HighscoreDto> newHighscores = new ArrayList<HighscoreDto>();
		HighscoreDto currentScore = newHighscore;
		if(highscores != null){
			for(HighscoreDto highscore : highscores.highscores){
				if(highscore.compareTo(currentScore) > 0){
					newHighscores.add(highscore);
				}else if(highscore.compareTo(currentScore) == 0){
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
		Collections.sort(newHighscores);
		Collections.reverse(newHighscores);
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
		HighscoresDto scores = json.fromJson(HighscoreManager.HighscoresDto.class, scoresString);
		if(scores != null){
			for(HighscoreDto highscore : scores.highscores){
				if(highscore.score == 0){
					highscore.score = highscore.roundsSurvived * 100 + highscore.heroUnit.unitsKilled;
				}
			}
		}
		return scores;
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
	
	private static HighscoreDto GetHighscoreDto(int score, UnitDto hero){
		HighscoreDto highscore = new HighscoreDto();
		highscore.score = score;
		highscore.heroUnit = hero;
		return highscore;
	}
	
	public static class HighscoresDto{
		public List<HighscoreDto> highscores;
	}
	
	public static class HighscoreDto implements Comparable<HighscoreDto>{
		public int roundsSurvived;
		public int score;
		public UnitDto heroUnit;
		
		@Override
		public int compareTo(HighscoreDto other) {
			return this.score - other.score;
		}
	}
	
	private static final String fileName = "heroesemblem.sav";
}
