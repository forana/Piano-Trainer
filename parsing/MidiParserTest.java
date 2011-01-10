package crescendo.base.parsing;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.base.song.Note;

public class MidiParserTest
{
	@Test
	public void properLoad() throws IOException
	{
		SongModel model=SongFactory.generateSongFromFile("resources/Don'tSpeak.mid");
		System.out.println("Song name: "+model.getTitle());
		System.out.println("Number of tracks: "+model.getTracks().size());
		for (Track track : model.getTracks())
		{
			System.out.println(track.getName()+":");
			for (int i=0; i<track.getNotes().size() && i<30; i++) // 8 measures of 3/4
			{
				Note note=track.getNotes().get(i);
				System.out.println("\tN "+note.getPitch()+"\tP? "+(note.isPlayable()?"Y":"N")+"\tD "+note.getDuration()+"\tM "+(note.getModifiers().size()>0?note.getModifiers().get(0):"-"));
			}
		}
		
		Assert.assertNotNull(model);
	}
}
