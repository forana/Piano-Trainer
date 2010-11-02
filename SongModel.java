package crescendo.base;

import java.util.List;

/**
 * A SongModel represents one entire piece of piece, including all of its tracks and metadata.
 * 
 * @author forana
 */
public class SongModel
{
	/**
	 * The tracks stored by this model.
	 */
	private List<Track> tracks;
	
	/**
	 * Creates a SongModel.
	 * 
	 * @param tracks
	 * @param title
	 * @param author
	 * @param email
	 * @param website
	 * @param license
	 * @param beatsPerMinute
	 * 
	 * TODO Finish this comment.
	 */
	public SongModel(List<Track> tracks,String title,String author,String email,String website,String license,int beatsPerMinute)
	{
		this.tracks=tracks;
	}
	
	/**
	 * The number of tracks in the model.
	 * 
	 * @return The number of tracks in the model.
	 */
	public int getNumTracks()
	{
		return this.tracks.size();
	}
	
	/**
	 * Returns an indexed track in the model.
	 * 
	 * @param track The index of the track. This value ranges from 0 to n-1, where n is the number of tracks in the model.
	 * 
	 * @return The track at the provided index, or null if the index is out of bounds.
	 */
	public Track getTrack(int track)
	{
		// TODO Implement null condition, or convert this to use an exception
		return this.tracks.get(track);
	}
	
	// TODO Implement metadata functions.
}
