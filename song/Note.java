package crescendo.base.song;

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
	 * Creates a Note.
	 * 
	 * @param pitch The MIDI pitch of the note.
	 * @param duration The duration of the note, in beats.
	 * @param dynamic The dynamic of the note.
	 * @param track The track which contains the note.
	 */
	public Note(int pitch,double duration,int dynamic,Track track)
	{
		this.pitch=pitch;
		this.dynamic=dynamic;
		this.track=track;
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
}
