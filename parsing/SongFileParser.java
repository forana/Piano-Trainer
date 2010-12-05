package crescendo.base.parsing;

import crescendo.base.SongModel;
import java.io.File;
import java.io.IOException;

public interface SongFileParser
{
	public SongModel parse(File file) throws IOException;
}
