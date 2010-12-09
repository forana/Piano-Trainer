package crescendo.base.EventDispatcher;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;

import crescendo.base.ErrorHandler;

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

	private static EventDispatcher dispatcher=null;
	
	private List<MidiEventListener> midiListeners;
	private List<InputEventListener> inputListeners;
	private MidiReceiver midiReceiver;
	private MidiDevice midiDevice;
	private List<MidiDevice> transmitterDevices;
	
	private Modifier currentModifer;
	
	
	/**
	 * EventDispatcher
	 * 
	 * Basic constructor
	 */
	private EventDispatcher()
	{
		midiListeners = new ArrayList<MidiEventListener>();
		inputListeners = new ArrayList<InputEventListener>();
		
		// instantiate the receiver object
		this.midiReceiver=new MidiReceiver();
		// load the device list
		this.loadTransmitterDevices();
		// default to the first device's transmitter
		this.midiDevice=null;
		this.setTransmitterDevice(this.transmitterDevices.get(0));
		
		currentModifer = new Modifier(false,false,false);
	}
	
	/**
	 * getInstance
	 * 
	 * @return dispatcher - singleton EventDispatcher
	 */
	public static EventDispatcher getInstance()
	{
		if(dispatcher==null)
		{
			synchronized (EventDispatcher.class)
			{
				if (dispatcher==null)
				{
					dispatcher = new EventDispatcher();
				}
			}
		}
		
		return dispatcher;
	}
	
	/**
	 * Analyzes all midi devices attached to this system, and stores a list
	 * of the devices that support transmitters.
	 */
	public void loadTransmitterDevices()
	{
		MidiDevice.Info[] devices=MidiSystem.getMidiDeviceInfo();
		List<MidiDevice> deviceList=new ArrayList<MidiDevice>();
		
		for (int i=0; i<devices.length; i++)
		{
			try
			{
				MidiDevice device=MidiSystem.getMidiDevice(devices[i]);
				
				// check how many transmitters this device supports.
				// -1 signifies unlimited receivers, so really  0
				// is the value we care about.
				if (device.getMaxTransmitters()!=0)
				{
					deviceList.add(device);
				}
			}
			catch (MidiUnavailableException e)
			{
				System.err.println("MidiDevice \""+devices[i].getName()+"\" could not be found.");
				String title="MIDI Device in use";
				String message="The device \""+devices[i].getName()+"\" is in use by another program.";
				if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.RETRY)
				{
					// try this device again
					i--;
				}
			}
		}
	}
	
	/**
	 * Returns a list of all MidiDevices that support transmitters. This list
	 * is updated when loadTransmitterDevices is called.
	 * 
	 * @return A list of all MidiDevices that support transmitters. This list
	 * is updated when loadTransmitterDevices is called.
	 */
	public List<MidiDevice> getTransmitterDevices()
	{
		return this.transmitterDevices;
	}
	
	/**
	 * Sets the transmitter to be listened on for midi events, and closes any
	 * existing transmitter.
	 * 
	 * @param device The device to use for the new transmitter.
	 * @return Whether or not the device was successfully set.
	 */
	public boolean setTransmitterDevice(MidiDevice device)
	{
		if (this.midiDevice!=null)
		{
			this.midiDevice.close();
		}
		
		this.midiDevice=device;
		
		boolean success=true;
		
		try
		{
			this.midiDevice.open();
			
			Transmitter transmitter=this.midiDevice.getTransmitter();
			transmitter.setReceiver(this.midiReceiver);
		}
		catch (MidiUnavailableException e)
		{
			String title="MIDI Device in use";
			String message="The specified device is in use by another program.";
			if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.RETRY)
			{
				return this.setTransmitterDevice(device);
			}
			else
			{
				success=false;
			}
		}
		
		return success;
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
	private void dispatchMidiEvent(MidiEvent midiEvent)
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
	private void dispatchInputEvent(InputEvent inputEvent)
	{
		for(InputEventListener listeners:inputListeners)
		{
			listeners.handleInputEvent(inputEvent);
		}
	}
	
	/**
	 * Internal class to be attached to a transmitter device. Feeds midi events
	 * back into the EventDispatcher.
	 * 
	 * @author forana
	 */
	private class MidiReceiver implements Receiver // javax.sound.midi.Receiver
	{
		private static final int OPCODE_MASK = 0xF0;
		private static final int OPCODE_OFF = 0x80;
		private static final int OPCODE_ON = 0x90;
		
		public MidiReceiver()
		{
		}
		
		// is needed for the interface, but it's empty LOL
		public void close()
		{
		}
		
		public void send(MidiMessage midiMessage,long timestamp)
		{
			byte[] message=midiMessage.getMessage();
			
			int opcode=message[0] & OPCODE_MASK;
			
			// If the code is note off or note on, handle it. we don't care about anything else.
			if (opcode == OPCODE_OFF || opcode == OPCODE_ON)
			{
				int note=message[1];
				int velocity=message[2];
				ActionType action;
				// determine whether the note is being pressed 
				// an alternate method of specifying off is using the on code with a velocity of zero
				if (opcode == OPCODE_OFF || velocity ==0)
				{
					action=ActionType.RELEASE;
				}
				else
				{
					action=ActionType.PRESS;
				}
				
				// create event object
				MidiEvent event=new MidiEvent(note,velocity,action);
				
				// dispatch event
				dispatchMidiEvent(event);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT)currentModifer.setShift(true);
		if(arg0.getKeyCode()==KeyEvent.VK_ALT)currentModifer.setAlt(true);
		if(arg0.getKeyCode()==KeyEvent.VK_CONTROL)currentModifer.setCtrl(true);
		
		KeyboardEvent event = new KeyboardEvent(ActionType.PRESS, System.currentTimeMillis(), 
				InputType.KEYBOARD, arg0.getKeyCode(), currentModifer);
		
		dispatchInputEvent(event);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT)currentModifer.setShift(false);
		else if(arg0.getKeyCode()==KeyEvent.VK_ALT)currentModifer.setAlt(false);
		else if(arg0.getKeyCode()==KeyEvent.VK_CONTROL)currentModifer.setCtrl(false);
		
		KeyboardEvent event = new KeyboardEvent(ActionType.RELEASE, System.currentTimeMillis(), 
				InputType.KEYBOARD, arg0.getKeyCode(), currentModifer);
		
		dispatchInputEvent(event);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//nothing	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//nothing	
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//nothing	
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		//nothing	
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		crescendo.base.EventDispatcher.MouseEvent event = new crescendo.base.EventDispatcher.MouseEvent(ActionType.PRESS, System.currentTimeMillis(), 
				InputType.CLICK, arg0.getButton(),arg0.getX(),arg0.getY(), currentModifer);
		
		dispatchInputEvent(event);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		crescendo.base.EventDispatcher.MouseEvent event = new crescendo.base.EventDispatcher.MouseEvent(ActionType.RELEASE, System.currentTimeMillis(), 
				InputType.CLICK, arg0.getButton(),arg0.getX(),arg0.getY(), currentModifer);
		
		dispatchInputEvent(event);
		
	}
}
