package crescendo.base.song;

import java.util.Iterator;
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
	
	/** Artists who worked on this particular song */
	private List<Creator> creators;
	
	/** time signature of this song */
	private TimeSignature timeSignature;
	private String title;
	private String email;
	private String website;
	private String license;
	private int bpm;
	
	/**
	 * Creates a SongModel.
	 * 
	 * @param tracks
	 * @param title
	 * @param creators
	 * @param email
	 * @param website
	 * @param license
	 * @param beatsPerMinute
	 * @param timeSignature
	 * TODO Finish this comment.
	 */
	public SongModel(List<Track> tracks,String title,List<Creator> creators,String email,String website,String license,int beatsPerMinute, TimeSignature timeSignature) {
		this.tracks=tracks;
		this.creators=creators;
		this.timeSignature = timeSignature;
		this.title=title;
		this.email=email;
		this.website=website;
		this.license=license;
		this.bpm=beatsPerMinute;
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
	
	/**
	 * getter for the list of creators
	 * @return list of creators of the song
	 */
	public List<Creator> getCreators()
	{
		return this.creators;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public String getWebsite()
	{
		return this.website;
	}
	
	public String getLicense()
	{
		return this.license;
	}
	
	public int getBPM()
	{
		return this.bpm;
	}
	
	public SongIterator iterator() {
		return new SongIterator();
	}
	
	private class SongIterator implements Iterator<Note> {
		
		public SongIterator() {
			
		}
		
		@Override
		public boolean hasNext() {	
			return false;
		}

		@Override
		public Note next() {
			return null;
		}

		@Override
		public void remove() {	
			
		}
	}
}
