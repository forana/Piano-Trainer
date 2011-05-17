package crescendo.base.EventDispatcher;

/**
 * InputEventListener
 * 
 * This interface represent the requirement to receive
 * InputEvents from the EventDispatcher.
 * 
 * @author groszc
 *
 */
public interface InputEventListener {
	
	/**
	 * handleInputEvent
	 * 
	 * This method is responsible for accepting the InputEvents being
	 * generated from the EventDispatcher.
	 * 
	 * @param inputEvent - the event that has occurred and needs to be handled
	 */
	public void handleInputEvent(InputEvent inputEvent);
}
