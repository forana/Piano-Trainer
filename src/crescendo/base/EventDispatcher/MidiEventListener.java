package crescendo.base.EventDispatcher;

/**
 * MidiEventListener
 * 
 *
 * This interface represent the requirement to receive
 * MidiEvents from the EventDispatcher.
 * 
 * @author groszc
 *
 */
public interface MidiEventListener {
	
	/**
	 * handleMidiEvent
	 * 
	 * This method is responsible for accepting the MidiEvents being
	 * generated from the EventDispatcher.
	 * 
	 * @param midiEvent - the event that has occurred and needs to be handled
	 */
	public void handleMidiEvent(MidiEvent midiEvent);
}
