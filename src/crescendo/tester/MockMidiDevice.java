package crescendo.tester;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import crescendo.base.EventDispatcher.MidiEvent;

// Make this a singleton
/**
 * This class pretends to be a mididevice and sends out the correct notes to the song being played by the program.
 * It can also sent out incorrect notes if they are specified in the console
 */
public class MockMidiDevice implements MidiDevice{

	private static MockTransmitter transmitter;
	private int maxTransmitters;
	private int maxReceivers;
	private static boolean isCreated = false;
	private static MockMidiDevice instance;
	private Info info;
	private boolean isOpen;
	
	
	private MockMidiDevice() {
		isOpen = false;
		maxTransmitters = 1;
		maxReceivers = 0;
		transmitter = MockTransmitter.getInstance();
		isCreated = true;
		info = new MockInfo("Automated Tester", "Patrick Larkin", "Testing", "0.00");

	}


	@Override
	public void close() {
		transmitter.close();	
	}

	@Override
	public Info getDeviceInfo() {
		return info;
		
	}

	@Override
	public int getMaxReceivers() {
		return maxReceivers;
	}

	@Override
	public int getMaxTransmitters() {
		return maxTransmitters;
	}

	@Override
	public long getMicrosecondPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Receiver getReceiver() throws MidiUnavailableException {
		// TODO Auto-generated method stub
		return transmitter.getReceiver();
	}

	@Override
	public List<Receiver> getReceivers() {
		List<Receiver> r = new ArrayList<Receiver>();
		r.add(transmitter.getReceiver());
		return r;
	}

	@Override
	public Transmitter getTransmitter() throws MidiUnavailableException {
		// TODO Auto-generated method stub
		return transmitter;
	}

	@Override
	public List<Transmitter> getTransmitters() {
		List<Transmitter> l = new ArrayList<Transmitter>();
		l.add(transmitter);
		return l;
	}

	@Override
	public boolean isOpen() {
		
		return isOpen;
	}

	@Override
	public void open() throws MidiUnavailableException {
		
		isOpen = true;
	}

	/**
	 * Returns the instance if one is created, otherwise creates one and returns it.
	 * @return
	 */
	public static MockMidiDevice getInstance() {
		if(!isCreated){
			instance = new MockMidiDevice();
		}
		return instance;
	}

	public class MockInfo extends MidiDevice.Info{
		
		public MockInfo(String arg0, String arg1, String arg2, String arg3) {
			super(arg0, arg1, arg2, arg3);
			System.out.println(""+super.getName());
		}	
	}
}
