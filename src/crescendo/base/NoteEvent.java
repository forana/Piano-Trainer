package crescendo.base;

import crescendo.base.song.Note;

/**
 * A NoteEvent encapsulates a note and its timestamp in the song, 
 * 
 * @author forana
 */
public class NoteEvent
{
	/**
	 * The Note involved in this event. 
	 */
	private Note note;
	
	/**
	 * The timestamp at which event this note occurs.
	 */
	private long timestamp;
	
	/**
	 * The action this event represents.
	 */
	private NoteAction action;
	
	// TODO Implement protected decorator variables.
	
	/**
	 * Creates a NoteEvent.
	 * 
	 * @param note
	 * @param action
	 * @param timestamp
	 * 
	 * TODO Finish this comment.
	 */
	public NoteEvent(Note note,NoteAction action,long timestamp)
	{
		this.note=note;
		this.action=action;
		this.timestamp=timestamp;
	}
	
	/**
	 * The note encapsulated by this event.
	 * 
	 * @return The note encapsulated by this event.
	 */
	public Note getNote()
	{
		return this.note;
	}
	
	/**
	 * The action involved in this event.
	 * 
	 * @return The action involved in this event.
	 */
	public NoteAction getAction()
	{
		return this.action;
	}
	
	/**
	 * The timestamp at which this event is supposed to take place.
	 * 
	 * @return The timestamp at which this event is supposed to take place.
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	/**
	 * Set the time at which this event is supposed to take place (milliseconds from epoch)
	 */
	public void setTimestamp(long time){
		timestamp = time;
	}
}
