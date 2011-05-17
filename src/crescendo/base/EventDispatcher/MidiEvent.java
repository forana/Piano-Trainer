package crescendo.base.EventDispatcher;

/**
 * MidiEvent encapsulates a musical note, the action involved, and the velocity, as well as the time at which the event occurred.
 * 
 * @author forana
 */
public class MidiEvent
{
	/**
	 * The note involved.
	 */
	private int note;
	
	/**
	 * The velocity of the note.
	 */
	private int velocity;
	
	/**
	 * The time in milliseconds at which this event occurred.
	 */
	private long timestamp;
	
	/**
	 * The action associated with this event.
	 */
	private ActionType action;
	
	/**
	 * Creates a new MidiEvent.
	 * 
	 * @param note The MIDI note associated with this event.
	 * @param velocity The velocity of the note.
	 * @param action The action associated with this event.
	 */
	public MidiEvent(int note,int velocity,ActionType action)
	{
		this.note=note;
		this.velocity=velocity;
		this.timestamp=System.currentTimeMillis();
		this.action=action;
	}
	
	/**
	 * The note associated with this event.
	 * 
	 * @return The note associated with this event.
	 */
	public int getNote()
	{
		return this.note;
	}
	
	/**
	 * The velocity at which the note was played.
	 * 
	 * @return The velocity at which the note was played.
	 */
	public int getVelocity()
	{
		return this.velocity;
	}
	
	/**
	 * The time at which this event occurred, in milliseconds.
	 * 
	 * @return The time at which this event occurred, in milliseconds since the unix epoch.
	 */
	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	/**
	 * The action associated with this event.
	 * 
	 * @return The action associated with this event.
	 */
	public ActionType getAction()
	{
		return this.action;
	}
}
