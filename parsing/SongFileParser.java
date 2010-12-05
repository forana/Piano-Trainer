package crescendo.base.parsing;

import java.io.File;
import java.io.IOException;

import crescendo.base.song.SongModel;

public interface SongFileParser
{
	public SongModel parse(File file) throws IOException;
}
