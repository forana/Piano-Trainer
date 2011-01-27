package crescendo.base.parsing;

import crescendo.base.SongPlayer;
import crescendo.base.AudioPlayer;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

/** Use this class for testing how something sounds, nothing more. */
public class PlayTest
{
	public static void main(String[] args)
	{
		try
		{
			SongModel model=SongFactory.generateSongFromFile("resources/pkmn-rt1.mid");
			SongPlayer player=new SongPlayer(model);
			AudioPlayer audio=new AudioPlayer(model,null);
			player.attach(audio,100);
			player.play();
			Thread.sleep(300000); // 5 minutes
		}
		catch (Exception e)
		{
		}
	}
}
