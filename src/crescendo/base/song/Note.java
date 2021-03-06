package crescendo.base.song;

import crescendo.base.song.modifier.NoteModifier;
import java.util.List;
import java.util.LinkedList;

/**
 * A note represents a single musical note.
 * 
 * @author forana
 */
public class Note
{
	/**
	 * The pitch of the note, using MIDI pitch (e.g. 60 is middle C).
	 */
	private int pitch;
	
	/**
	 * The dynamic of the note, aka the softness/hardness.
	 */
	private int dynamic;
	
	/**
	 * The track that contains the note.
	 */
	private Track track;
	
	/**
	 * The duration of this note, in beats.
	 */
	private double duration;
	
	/**
	 * This note's modifiers.
	 */
	private List<NoteModifier> modifiers;
	
	private boolean playable;
	
	/**
	 * Creates a playable Note.
	 * 
	 * @param pitch The MIDI pitch of the note.
	 * @param duration The duration of the note, in beats.
	 * @param dynamic The dynamic of the note.
	 * @param track The track which contains the note.
	 */
	public Note(int pitch,double duration,int dynamic,Track track)
	{
		this(pitch,duration,dynamic,track,true);
	}
	
	/**
	 * Creates a Note that may or may not be playable.
	 * 
	 * @param pitch The MIDI pitch of the note.
	 * @param duration The duration of the note, in beats.
	 * @param dynamic The dynamic of the note.
	 * @param track The track which contains the note.
	 * @param playable Whether or not this note is playable.
	 */
	public Note(int pitch,double duration,int dynamic,Track track,boolean playable)
	{
		this.pitch=pitch;
		this.dynamic=dynamic;
		this.track=track;
		this.modifiers=new LinkedList<NoteModifier>();
		this.playable=playable;
		this.duration = duration;
	}
	
	/**
	 * The pitch of the note.
	 * 
	 * @return The pitch of the note.
	 */
	public int getPitch()
	{
		return this.pitch;
	}
	
	/**
	 * The dynamic of the note.
	 * 
	 * @return The dynamic of the note.
	 */
	public int getDynamic()
	{
		return this.dynamic;
	}
	
	/**
	 * The track that contains this note.
	 * 
	 * @return The track that contains this note.
	 */
	public Track getTrack()
	{
		return this.track;
	}
	
	/**
	 * The duration of the note.
	 * 
	 * @return The duration of the note, in beats.
	 */
	public double getDuration()
	{
		return this.duration;
	}
	
	/**
	 * Add a modifier to this note.
	 * 
	 * @param modifier The modifier to add.
	 */
	public void addModifier(NoteModifier modifier)
	{
		this.modifiers.add(modifier);
	}
	
	/**
	 * Gets all modifiers attached to this note.
	 * 
	 * @return All modifiers for this note.
	 */
	public List<NoteModifier> getModifiers()
	{
		return this.modifiers;
	}
	
	/**
	 * Whether or not this note should be played.
	 * 
	 * @return true if this note should be played, false otherwise.
	 */
	public boolean isPlayable()
	{
		return this.playable;
	}
}
