package crescendo.base.EventDispatcher;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.LinkedList;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;

import crescendo.base.ErrorHandler;
import crescendo.base.profile.ProfileManager;

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
 * @author forana
 */

public class EventDispatcher implements KeyListener,MouseListener {

	/** singleton EevntDispatcher **/
	private static EventDispatcher dispatcher=null;
	
	/** list of MidiEventListeners to send MidiEvents to **/
	private List<MidiEventListener> midiListeners;
	
	/** list of InputEventListeners to send InputEvents to **/
	private List<InputEventListener> inputListeners;
	
	/** the current midi receiver **/
	private MidiReceiver midiReceiver;
	
	/** the current midi device **/
	private MidiDevice midiDevice;
	
	/** a list of devices **/
	private List<MidiDevice> transmitterDevices;
	
	/** holds the current event modifiers (if any) **/
	private Modifier currentModifer;
	
	
	/**
	 * EventDispatcher
	 * 
	 * Basic constructor
	 */
	private EventDispatcher()
	{
		midiListeners = new LinkedList<MidiEventListener>();
		inputListeners = new LinkedList<InputEventListener>();
		
		// instantiate the receiver object
		this.midiReceiver=new MidiReceiver();
		// load the device list
		this.loadTransmitterDevices();
		// default to the first device's transmitter
		this.midiDevice=null;
		
		// attempt to load device from preferences
		String deviceName=ProfileManager.getInstance().getActiveProfile().getMidiDeviceName();
		for (MidiDevice device : this.transmitterDevices)
		{
			if (device.getDeviceInfo().getName().equals(deviceName))
			{
				this.setTransmitterDevice(device);
				break;
			}
		}
		if (this.midiDevice==null)
		{
			this.setTransmitterDevice(this.transmitterDevices.get(0));
			if (deviceName!=null && !"".equals(deviceName) && !deviceName.startsWith("com.sun"))
			{
				ErrorHandler.showNotification("\""+deviceName+"\" not found","The MIDI device \""+midiDevice+"\" was not found.\nUsing \""+this.midiDevice.getDeviceInfo().getName()+"\" instead.");
			}
		}
		
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
	 * loadTransmitterDevices
	 * 
	 * Analyzes all midi devices attached to this system, and stores a list
	 * of the devices that support transmitters.
	 */
	public void loadTransmitterDevices()
	{
		MidiDevice.Info[] devices=MidiSystem.getMidiDeviceInfo();
		this.transmitterDevices=new LinkedList<MidiDevice>();
		
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
					this.transmitterDevices.add(device);
				}
			}
			catch (MidiUnavailableException e)
			{
				System.err.println("MidiDevice \""+devices[i].getName()+"\" could not be found.");
				String title="MIDI Device in use";
				String message="The device \""+devices[i].getName()+"\" is in use by another program.";
				if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.Response.RETRY)
				{
					// try this device again
					i--;
				}
			}
		}
	}
	
	/**
	 * getTransmitterDevices
	 * 
	 * 
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
	
	public MidiDevice getCurrentTransmitterDevice()
	{
		return this.midiDevice;
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
			for (Transmitter transmitter : this.midiDevice.getTransmitters())
			{
				transmitter.getReceiver().close();
				transmitter.close();
			}
			this.midiDevice.close();
		}
		
		this.midiDevice=device;
		
		boolean success=true;
		
		if (this.midiDevice!=null)
		{
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
				if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.Response.RETRY)
				{
					return this.setTransmitterDevice(device);
				}
				else
				{
					success=false;
				}
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
		
		/** Creates a new receiver, ready to handle events. */
		public MidiReceiver()
		{
		}
		
		/** Does nothing. */
		public void close()
		{
		}
		
		/**
		 * Parses a midiMessage and creates a MidiEvent with the resulting information, if the message symbolizes
		 * either a note on or note off event.
		 * 
		 * This method is intended only to be called by a Transmitter object.
		 * 
		 * @param midiMessage The message.
		 * @param timestamp The time (in ms) at which this input occurred.
		 */
		public void send(MidiMessage midiMessage,long timestamp)
		{
			byte[] message=midiMessage.getMessage();
			
			int opcode=message[0] & OPCODE_MASK;
			
			// If the code is note off or note on, handle it. we don't care about anything else.
			if (opcode == OPCODE_OFF || opcode == OPCODE_ON)
			{
				int note=message[1]&0x7F;
				int velocity=message[2]&0x7F;
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
		//update the currentModifier if a modifier has been pressed
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT)currentModifer.setShift(true);
		if(arg0.getKeyCode()==KeyEvent.VK_ALT)currentModifer.setAlt(true);
		if(arg0.getKeyCode()==KeyEvent.VK_CONTROL)currentModifer.setCtrl(true);
		
		//create a new keyboard event
		KeyboardEvent event = new KeyboardEvent(ActionType.PRESS, System.currentTimeMillis(), 
				InputType.KEYBOARD, arg0.getKeyCode(), currentModifer);
		
		//send out the keyboard event!
		dispatchInputEvent(event);
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		//update the currentModifier if a modifier has been removed
		if(arg0.getKeyCode()==KeyEvent.VK_SHIFT)currentModifer.setShift(false);
		else if(arg0.getKeyCode()==KeyEvent.VK_ALT)currentModifer.setAlt(false);
		else if(arg0.getKeyCode()==KeyEvent.VK_CONTROL)currentModifer.setCtrl(false);
		
		//create a new keyboard event
		KeyboardEvent event = new KeyboardEvent(ActionType.RELEASE, System.currentTimeMillis(), 
				InputType.KEYBOARD, arg0.getKeyCode(), currentModifer);
		
		//send out the keyboard event!
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
		//create a new mouse event
		crescendo.base.EventDispatcher.MouseEvent event = new crescendo.base.EventDispatcher.MouseEvent(ActionType.PRESS, System.currentTimeMillis(), 
				InputType.CLICK, arg0.getButton(),arg0.getX(),arg0.getY(), currentModifer);
		
		//send out the mouse event!
		dispatchInputEvent(event);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//create a new mouse event
		crescendo.base.EventDispatcher.MouseEvent event = new crescendo.base.EventDispatcher.MouseEvent(ActionType.RELEASE, System.currentTimeMillis(), 
				InputType.CLICK, arg0.getButton(),arg0.getX(),arg0.getY(), currentModifer);
		
		//send out the mouse event!
		dispatchInputEvent(event);
		
	}
	
	/**
	 * Detects a midi device, by setting the first device to respond as the new transmitter device.
	 * This is a blocking call; it will block until either a device is detected or a specified
	 * amount of time has passed.
	 * 
	 * @param timeout The amount of time for which to detect (in seconds).
	 */
	public void detectMidiDevice(int timeout)
	{
		int cycles=timeout*10;
		// clear out the current so we don't run into usage issues
		this.setTransmitterDevice(null);
		
		// get em all listening
		for (MidiDevice device : this.transmitterDevices)
		{
			try
			{
				device.open();
				
				Transmitter transmitter=device.getTransmitter();
				transmitter.setReceiver(new MidiAutodetectionReceiver(this,device));
			}
			catch (MidiUnavailableException e)
			{
				System.err.println("Device '"+device.getDeviceInfo().getName()+"' could not be opened!");
			}
		}
		
		// try to time out
		int timesRun=0;
		while (this.midiDevice==null && timesRun<cycles)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// this should never happen, but don't panic if it does
			}
			timesRun++;
		}
		
		// regardless of whether we timed out or not, need to remove all of these receivers we just added
		for (MidiDevice device : this.transmitterDevices)
		{
			// don't close the active one, that was the point of all of thise
			if (device!=this.midiDevice)
			{
				for (Transmitter transmitter : device.getTransmitters())
				{
					transmitter.getReceiver().close();
					transmitter.close();
				}
				device.close();
			}
		}
	}
	
	/**
	 * Provides a receiver intended to be added to multiple transmitters that will set the transmitter of the
	 * calling EventDispatcher when a key press is detected.
	 */
	private class MidiAutodetectionReceiver implements Receiver
	{
		/** The calling EventDispatcher. */
		private EventDispatcher dispatcher;
		
		/** The device this receiver is responsible for. */
		private MidiDevice responsibleDevice;
		
		/** Set if this receiver has already set the device, false otherwise. */
		private boolean sent;
		
		/**
		 * Creates a new receiver.
		 * 
		 * @param dispatcher The EventDispatcher of which to set the transmitter device.
		 * @param device The MidiDevice this receiver represents.
		 */
		public MidiAutodetectionReceiver(EventDispatcher dispatcher,MidiDevice device)
		{
			this.dispatcher=dispatcher;
			this.responsibleDevice=device;
			this.sent=false;
		}
		
		/** Does nothing. **/
		public void close()
		{
		}
		
		/**
		 * Receives a message from a transmitter.
		 * 
		 * @param message The message.
		 * @param timestmap The time (in ms) at which this message was instantiated.
		 */
		public void send(MidiMessage message,long timestamp)
		{
			// only send it once; without this it sends twice sometimes before this receiver can be destroyed,
			// creating an internal off-by-1... quite fun
			if (!this.sent)
			{
				// this will remove this listener as a byproduct
				this.dispatcher.setTransmitterDevice(this.responsibleDevice);
				this.sent=true;
			}
		}
	}
}
