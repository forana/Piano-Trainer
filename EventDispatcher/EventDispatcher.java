package crescendo.base.EventDispatcher;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * EventDispatcher
 * 
 * 	This class serves as a global event listener for each event,
 * 	both midi and key/mouse, and relays these events  to all of the 
 * 	classes that attach to this class.
 *  
 *  EventDispatcher is a singleton.
 *
 * @author groszc
 *
 */

public class EventDispatcher implements KeyListener,MouseListener {

	private EventDispatcher dispatcher=null;
	private ArrayList<MidiEventListener> midiListeners;
	private ArrayList<InputEventListener> inputListeners;
	
	
	/**
	 * EventDispatcher
	 * 
	 * Basic constructor
	 */
	private EventDispatcher()
	{
		midiListeners = new ArrayList<MidiEventListener>();
		inputListeners = new ArrayList<InputEventListener>();
	}
	
	
	/**
	 * getInstance
	 * 
	 * @return dispatcher - singleton EventDispatcher
	 */
	public EventDispatcher getInstance()
	{
		if(dispatcher==null)
		{
			dispatcher = new EventDispatcher();
		}
		
		return dispatcher;
	}
	
	
	/**
	 * registerComponent
	 * 
	 * @param c - the component to listen on
	 */
	public void registerComponent(Component c)
	{
		c.addKeyListener(this);
		c.addMouseListener(this);
	}
	
	
	/**
	 * unregisterComponent
	 * 
	 * @param c - the component to stop listening on
	 */
	public void unregisterComponent(Component c)
	{
		c.removeKeyListener(this);
		c.removeMouseListener(this);
	}
	
	
	/**
	 * attach
	 * 
	 * The EventDispatcher adds a MidiEventListener to it's list of 
	 * MidiEventListeners.
	 * 
	 * @param midiListener - the MidiEventListener to add to the dispatcher
	 */
	public void attach(MidiEventListener midiListener)
	{
		midiListeners.add(midiListener);
	}
	
	/**
	 * detach
	 * 
	 * The EventDispatcher removes a MidiEventListener from it's list of 
	 * MidiEventListeners.
	 * 
	 * @param midiListener - the MidiEventListener to remove from the dispatcher
	 */
	public void detach(MidiEventListener midiListener)
	{
		midiListeners.remove(midiListener);
	}
	
	
	/**
	 * attach
	 * 
	 * The EventDispatcher adds an InputEventListener to it's list of 
	 * InputEventListeners.
	 * 
	 * @param midiListener - the InputEventListener to add to the dispatcher
	 */
	public void attach(InputEventListener eventListener)
	{
		inputListeners.add(eventListener);
	}
	
	
	/**
	 * detach
	 * 
	 * The EventDispatcher removes an InputEventListener from it's list of 
	 * InputEventListeners.
	 * 
	 * @param midiListener - the InputEventListener to remove from the dispatcher
	 */
	public void detach(InputEventListener eventListener)
	{
		inputListeners.remove(eventListener);
	}
	
	
	/**
	 * dispatchMidiEvent
	 * 
	 * Takes a MidiEvent and dispatches it to all the 
	 * MidiEventListeners attached to the EventDispatcher.
	 * 
	 * @param midiEvent
	 */
	public void dispatchMidiEvent(MidiEvent midiEvent)
	{
		for(MidiEventListener listeners:midiListeners)
		{
			listeners.handleMidiEvent(midiEvent);
		}
	}
	
	
	/**
	 * dispatchInputEvent
	 * 
	 * Takes a InputEvent and dispatches it to all the 
	 * InputEventListeners attached to the EventDispatcher.
	 * 
	 * @param inputEvent
	 */
	public void dispatchInputEvent(InputEvent inputEvent)
	{
		for(InputEventListener listeners:inputListeners)
		{
			listeners.handleInputEvent(inputEvent);
		}
	}
}
