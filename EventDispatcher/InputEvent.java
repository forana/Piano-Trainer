package crescendo.base.EventDispatcher;


/**
 * InputEvent
 * 
 * The InputEvent abstract class represents a non-midi event and
 * documents some of the similarities between like events such as
 * mouse and keyboard.
 * 
 * @author groszc
 *
 */
public abstract class InputEvent {

	
	/** The time in milliseconds at which this event occurred. **/
	private long timestamp;
	
	
	/** The action associated with this event. **/
	private ActionType action;
	
	/** The action associated with this event. **/
	private InputType type;
	
	
	/**
	 * InputEvent
	 * 
	 * 	Default constructor for InputEvent.
	 * 
	 * @param a - action type
	 * @param t - timestamp
	 * @param i - input type
	 */
	InputEvent(ActionType a, long t, InputType i)
	{
		action = a;
		timestamp = t;
		type = i;
	}
	
}
