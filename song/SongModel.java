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
	
	private double totalDuration;
	
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
		
		totalDuration=0;
		if(tracks.size()>0){			
			List<Note> notes = tracks.get(0).getNotes();
			for(Note n : notes){
				totalDuration+=n.getDuration();
			}
		}
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
	
	/**
	 * Gets the title of the song.
	 * 
	 * @return The title of the song.
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * Gets the email address specified by this song.
	 * 
	 * @return The email address.
	 */
	public String getEmail()
	{
		return this.email;
	}
	
	/**
	 * Gets the website of for the specified song.
	 * 
	 * @return The URL for the song, in string form.
	 */
	public String getWebsite()
	{
		return this.website;
	}
	
	/**
	 * Gets the license with which this song was distributed.
	 * 
	 * @return The license, possibly just the name of it or an entire text.
	 */
	public String getLicense()
	{
		return this.license;
	}
	
	/**
	 * Gets the initial tempo of the song.
	 * 
	 * @return The initial tempo of the song in beats per minute.
	 */
	public int getBPM()
	{
		return this.bpm;
	}
	
	/**
	 * Gets the initial time signature of the song.
	 * 
	 * @return The initial time signature of the song.
	 */
	public TimeSignature getTimeSignature()
	{
		return this.timeSignature;
	}
	
	public SongIterator iterator() {
		return new SongIterator();
	}
	
	/**
	 * Gets the initial key signature of the song.
	 * 
	 * @return The initial key signature.
	 */
	public int getKeySignature(){
		return this.keySignature;
	}
	
	
	/**
	 * Gets the duration of the song in number of beats
	 * @return duration of the song in number of beats
	 */
	public double getDuration(){
		return totalDuration;	
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
