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
	
	/** Whether or not this pairing is to be considered 'correct'. **/
	private boolean correct;
	
	/**
	 * Creates a new event. Either parameter may be null, but not both.
	 * 
	 * @param expectedNote The note that was expected.
	 * @param playedNote The note the user played.
	 * @param correct Whether or note the note is correct.
	 */
	public ProcessedNoteEvent(NoteEvent expectedNote,MidiEvent playedNote,boolean correct)
	{
		if (expectedNote==null && playedNote==null)
		{
			throw new IllegalArgumentException("Making both parameters null makes a meaningless event object.");
		}
		this.playedNote=playedNote;
		this.expectedNote=expectedNote;
		this.correct=correct;
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
	 * The note the system expected.
	 * 
	 * @return The note that was expected, or null if no note was expected.
	 */
	public NoteEvent getExpectedNote()
	{
		return this.expectedNote;
	}
	
	/**
	 * Whether or not the note is correct.
	 * 
	 * @return true if the note was judged correct, false otherwise.
	 */
	public boolean isCorrect()
	{
		return this.correct;
	}
}
