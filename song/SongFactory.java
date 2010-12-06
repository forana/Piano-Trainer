package crescendo.base.song;

import java.io.File;
import java.io.IOException;

import crescendo.base.parsing.SongFileParser;
import crescendo.base.parsing.MidiParser;

public class SongFactory
{
	public static SongModel generateSongFromFile(String filename) throws IOException
	{
		SongFileParser parser=null;
		File file=new File(filename);
		if (filename.toLowerCase().endsWith(".xml"))
		{
			// do das musicXML
		}
		else if (filename.toLowerCase().endsWith(".mid") || filename.toLowerCase().endsWith(".midi"))
		{
			parser=new MidiParser();
		}
		else
		{
			throw new IOException("File is not of proper type");
		}
		return parser.parse(file);
	}
}
