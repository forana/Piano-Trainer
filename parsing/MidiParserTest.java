package crescendo.base.parsing;

import java.io.IOException;

import org.junit.Test;
import org.junit.Assert;

import crescendo.base.SongFactory;
import crescendo.base.SongModel;

public class MidiParserTest
{
	@Test
	public void properLoad() throws IOException
	{
		SongModel model=SongFactory.generateSongFromFile("D:/downloads/morrowind.mid");
		Assert.assertNotNull(model);
	}
}
