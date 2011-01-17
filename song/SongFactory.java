package crescendo.base.song;

import java.io.File;
import java.io.IOException;

import crescendo.base.parsing.MusicXmlParser;
import crescendo.base.parsing.SongFileParser;
import crescendo.base.parsing.MidiParser;

/**
 * Provides a method to read SongModel objects out of files without having to know the type needed.
 * 
 * @author forana
 * @author nickgartmann
 */
public class SongFactory
{
	/**
	 * Read a SongModel from a file.
	 * 
	 * @param filename The name of the file from which to read.
	 * 
	 * @throw IOException if the file is improperly formatted or a general file reading error occurs.
	 */
	public static SongModel generateSongFromFile(String filename) throws IOException
	{
		SongFileParser parser=null;
		File file=new File(filename);
		// xml is a temporary hack so I can test without having to add unraring - forana
		if (filename.toLowerCase().endsWith(".mxl") || filename.toLowerCase().endsWith(".xml"))
		{
			// do das musicXML
			parser = new MusicXmlParser();
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
