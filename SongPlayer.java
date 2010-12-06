package crescendo.base;

import crescendo.base.song.SongModel;

/**
 * A SongPlayer does the actual running through the song and propagation of events.
 * 
 * @author forana
 */
public class SongPlayer
{
	// TODO Pretty much all of this class
	
	/**
	 * The songed that is being played.
	 */
	private SongModel songModel;
	
	/**
	 * TODO This
	 * 
	 * @param songModel
	 */
	public SongPlayer(SongModel songModel)
	{
		this.songModel=songModel;
	}
	
	/**
	 * TODO This
	 */
	public SongModel getSong()
	{
		return this.songModel;
	}
}
