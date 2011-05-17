package crescendo.base.parsing;

import java.io.File;
import java.io.IOException;

import crescendo.base.song.SongModel;

/**
 * Provides a generalization for classes that read a SongModel out of a file.
 * 
 * @author forana
 */
public interface SongFileParser
{
	/**
	 * Parses a SongModel object out of a file.
	 * 
	 * @param file The file to parse.
	 * 
	 * @return The parsed object.
	 * 
	 * @throw IOException If the file is not properly formatted.
	 */
	public SongModel parse(File file) throws IOException;
}
