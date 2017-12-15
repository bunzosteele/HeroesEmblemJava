package bunzosteele.heroesemblem.model;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public final class MusicManager
{
	public static Music nowPlaying;
	public static String nowPlayingFile;
	
	public static void PlayMenuMusic(float volume){
		String newFile = "MainTheme.mp3";
		if(!newFile.equals(MusicManager.nowPlayingFile)){
			MusicManager.Dispose();
			MusicManager.nowPlaying = Gdx.audio.newMusic(Gdx.files.internal(newFile));
			MusicManager.nowPlayingFile = newFile;
			MusicManager.Play(volume);
		}
	}
	
	public static void PlayShopMusic(float volume){
		String newFile = "CrossroadsInn.mp3";
		if(!newFile.equals(MusicManager.nowPlayingFile)){
			MusicManager.Dispose();
			MusicManager.nowPlaying = Gdx.audio.newMusic(Gdx.files.internal(newFile));
			MusicManager.nowPlayingFile = newFile;
			MusicManager.Play(volume);
		}
	}
	
	public static void PlayEasyBattleMusic(float volume){
		String newFile = "MarchingMusic.mp3";
		if(!newFile.equals(MusicManager.nowPlayingFile)){
			MusicManager.Dispose();
			MusicManager.nowPlaying = Gdx.audio.newMusic(Gdx.files.internal(newFile));
			MusicManager.nowPlayingFile = newFile;
			MusicManager.Play(volume);
		}
	}
	
	public static void PlayHardBattleMusic(float volume){
		String newFile = "BattleMusic.mp3";
		if(!newFile.equals(MusicManager.nowPlayingFile)){
			MusicManager.Dispose();
			MusicManager.nowPlaying = Gdx.audio.newMusic(Gdx.files.internal(newFile));
			MusicManager.nowPlayingFile = newFile;
			MusicManager.Play(volume);
		}
	}
	
	private static void Dispose(){
		if(MusicManager.nowPlaying != null){
			MusicManager.nowPlaying.stop();
			MusicManager.nowPlaying.dispose();
		}
	}
	
	private static void Play(float volume){
		MusicManager.nowPlaying.setVolume(volume);
		MusicManager.nowPlaying.setLooping(true);
		MusicManager.nowPlaying.play();
	}
}
