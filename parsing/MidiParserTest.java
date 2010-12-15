package crescendo.base.parsing;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class MidiParserTest
{
	@Test
	public void properLoad() throws IOException
	{
		SongModel model=SongFactory.generateSongFromFile("resources/morrowind.mid");
		System.out.println("Song name: "+model.getTitle());
		System.out.println("Number of tracks: "+model.getTracks().size());
		
		Assert.assertNotNull(model);
	}
}
