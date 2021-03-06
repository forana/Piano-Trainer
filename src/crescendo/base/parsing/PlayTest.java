package crescendo.base.parsing;

import crescendo.base.SongPlayer;
import crescendo.base.AudioPlayer;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

/** Use this class for testing how something sounds, nothing more. */
public class PlayTest
{
	public static void main(String[] args) throws Exception
	{
		SongModel model=SongFactory.generateSongFromFile("resources/ode.mid");
		model.getTracks().remove(1);
		SongPlayer player=new SongPlayer(model);
		AudioPlayer audio=new AudioPlayer(model,null);
		player.attach(audio,100);
		player.play();
		System.out.println("Playing \""+model.getTitle()+"\"");
		Thread.sleep(300000); // 5 minutes
	}
}
