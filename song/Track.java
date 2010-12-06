package crescendo.base.song;

import java.util.List;

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
	public Track(String name,int voice,List<Note> notes)
	{
		this.name=name;
		this.voice=voice;
		this.notes=notes;
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
	
	// TODO implement TrackIterator functionality
}
