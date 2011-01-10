package crescendo.base;

import crescendo.base.EventDispatcher.MidiEvent;

/**
 * Pairs an expected note event with a midi event.
 * 
 * @author forana
 */
public class ProcessedNoteEvent
{
	/** The note the user played. **/
	private MidiEvent playedNote;
	
	/** The note it was paired to. **/
	private NoteEvent expectedNote;
	
	/**
	 * Creates a new event. Either parameter may be null, but not both.
	 * 
	 * @param expectedNote The note that was expected.
	 * @param playedNote The note the user played.
	 */
	public ProcessedNoteEvent(NoteEvent expectedNote,MidiEvent playedNote)
	{
		if (expectedNote==null && playedNote==null)
		{
			throw new IllegalArgumentException("Making both parameters null makes a meaningless event object.");
		}
		this.playedNote=playedNote;
		this.expectedNote=expectedNote;
	}
	
	/**
	 * The note the user played.
	 * 
	 * @return The note the user played, or null if the user did not play a note.
	 */
	public MidiEvent getPlayedNote()
	{
		return this.playedNote;
	}
	
	/**
	 * The note the system expected
	 * 
	 * @return The note that was expected, or null if no note was expected.
	 */
	public NoteEvent getExpectedNote()
	{
		return this.expectedNote;
	}
}
