package crescendo.base.song;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import crescendo.base.song.Track.TrackIterator;

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
	private int keySignature;
	
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
	public SongModel(List<Track> tracks,String title,List<Creator> creators,String email,String website,String license,int beatsPerMinute, TimeSignature timeSignature, int keySignature) {
		this.tracks=tracks;
		this.creators=creators;
		this.timeSignature = timeSignature;
		this.title=title;
		this.email=email;
		this.website=website;
		this.license=license;
		this.bpm=beatsPerMinute;
		this.keySignature = keySignature;
	}
	
	/**
	 * The tracks in the model.
	 * 
	 * @return The tracks.
	 */
	public List<Track> getTracks()
	{
		return this.tracks;
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
	
	public TimeSignature getTimeSignature()
	{
		return this.timeSignature;
	}

	public SongIterator iterator() {
		return new SongIterator();
	}
	
	public int getKeySignature(){
		return this.keySignature;
	}
	
	private class SongIterator implements Iterator<Note> {
		
		private int currentTrack;
		private Map<Track,TrackIterator> iterators;
		
		public SongIterator() {
			currentTrack = 0;
			iterators = new HashMap<Track,TrackIterator>();
			for(Track track : tracks){
				iterators.put(track, track.iterator());
			}
		}
		
		@Override
		public boolean hasNext() {	
			boolean hasNext = false;
			for(int i=0;i<tracks.size() && !hasNext; i++){
				hasNext = iterators.get(tracks.get(i)).hasNext();
			}
			return hasNext;
		}

		@Override
		public Note next() {
			Note returnNote = iterators.get(tracks.get(currentTrack)).next();
			if(currentTrack>=tracks.size()-1){
				currentTrack=0;
			}else{
				currentTrack++;
			}
			return returnNote;
		}
		
		public List<Note> next(int beats){
			List<Note> notes = new LinkedList<Note>();
			for(TrackIterator iter : iterators.values()){
				notes.addAll(iter.next(beats));
			}
			return notes;
		}

		@Override
		public void remove() {	
			throw new UnsupportedOperationException();
		}
	}
}
