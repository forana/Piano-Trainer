package crescendo.base.song;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A Track is a set of notes, all under one instrument. This is analogous to one part
 * for a song in sheet music.
 * 
 * @author forana
 */
public class Track
{
	/**
	 * The name of this track.
	 */
	private String name;

	/**
	 * The instrument voice of this track, indexed by standard MIDI voices.
	 */
	private int voice;

	/**
	 * The notes in this track.
	 */
	private List<Note> notes;

	/**
	 * Creates a new track.
	 * 
	 * @param name The name of this track.
	 * @param voice The instrument voice of this track, indexed by standard MIDI voices.
	 * @param notes The notes in this track.
	 */
	public Track(String name,int voice)
	{
		this.name=name;
		this.voice=voice;
		this.notes=new LinkedList<Note>();
	}

	/**
	 * The name of this track.
	 * 
	 * @return The name of this track.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * The instrument voice of this track.
	 * 
	 * @return The instrument voice of this track, indexed by standard MIDI voices.
	 */
	public int getVoice()
	{
		return this.voice;
	}

	/**
	 * The notes of this track.
	 * 
	 * @return The notes of this track.
	 */
	public List<Note> getNotes()
	{
		return this.notes;
	}
	
	/**
	 * Add a note to this track.
	 */
	public void addNote(Note note)
	{
		this.notes.add(note);
	}

	public TrackIterator iterator(){
		return new TrackIterator();
	}

	public class TrackIterator implements Iterator<Note> {
		private ListIterator<Note> iter;
		private double beatOffset;
		private double beatsPassed;
		
		public TrackIterator() {
			iter = notes.listIterator();
			beatOffset=0;
			beatsPassed=0;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		/**
		 * Test if the track has enough notes to fill the given number of beats
		 * @param beats the number of beats to fill
		 * @return if the track has enough notes to fill the requested number of beats
		 */
		public boolean hasNext(double beats) {
			// commenting this out for now; unless I'm mistaken this has the same effect
			// for a far lesser cost - forana
			/*double beatCount = 0;
			int iterCount = 0;
			while(iter.hasNext() && beatCount<beats){
				beatCount+=iter.next().getDuration();
				iterCount++;
			}
			for(;iterCount>0;iterCount--){
				iter.previous();
			}
			return beatCount>=beats;*/
			return iter.hasNext();
		}

		@Override
		public Note next() {
			return iter.next();
		}

		public double getOffset(){
			return beatOffset;
		}

		public List<Note> next(double beats) {
			if(beatOffset>0){
				beatOffset-=beats;
			}
			double beatCount=0;
			List<Note> nextNotes = new LinkedList<Note>();
			if(beatOffset<=0){
				while(iter.hasNext() && beatCount<(beats+beatOffset)){
					Note current = iter.next();
					nextNotes.add(current);
					beatCount+=current.getDuration();
				}
				beatOffset = beatCount - (beats+beatOffset);
			}
			beatsPassed+=beats;
			return nextNotes;
		}
		
		public double getBeatsPassed()
		{
			return beatsPassed;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
