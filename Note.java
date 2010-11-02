package crescendo.base;

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
	 * The dynamic of the note.
	 */
	
	/**
	 * Creates a Note.
	 * 
	 * @param pitch The MIDI pitch of the note.
	 * @param duration The duration of the note, in milliseconds.
	 * @param dynamic The dynamic of the note.
	 */
	public Note(int pitch,double duration,double dynamic)
	{
		this.pitch=pitch;
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
}
