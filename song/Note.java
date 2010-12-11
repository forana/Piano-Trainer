package crescendo.base.song;

/**
 * A note represents a single musical note.
 * 
 * @author forana
 */
public class Note
{
	// TODO Comment this and implement the rest of these
	// although mezzoforte is the best dynamic ever
	public static final double MEZZOFORTE = 0.6;
	
	/**
	 * The pitch of the note, using MIDI pitch (e.g. 60 is middle C).
	 */
	private int pitch;
	
	/**
	 * The dynamic of the note, aka the softness/hardness.
	 */
	private double dynamic;
	
	/**
	 * The track that contains the note.
	 */
	private Track track;
	
	/**
	 * Creates a Note.
	 * 
	 * @param pitch The MIDI pitch of the note.
	 * @param duration The duration of the note, in milliseconds.
	 * @param dynamic The dynamic of the note.
	 * @param track The track which contains the note.
	 */
	public Note(int pitch,double duration,double dynamic,Track track)
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
	public double getDynamic()
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
}
