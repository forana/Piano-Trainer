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
		SongModel model=SongFactory.generateSongFromFile("D:/downloads/morrowind.mid");
		Assert.assertNotNull(model);
	}
}
