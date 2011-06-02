package crescendo.tester;

import crescendo.base.EventDispatcher.ActionType;
import crescendo.base.EventDispatcher.MidiEvent;

public class MockMidiEvent extends MidiEvent {
	
	private long mockTimestamp;

	public MockMidiEvent(int note, int velocity, ActionType action) {
		super(note, velocity, action);
	}

	public void setTimestamp(long timestamp){
		this.mockTimestamp = timestamp;
	}
	
	/*
	 * (non-Javadoc)
	 * @see crescendo.base.EventDispatcher.MidiEvent#getTimestamp()
	 */
	@Override
	public long getTimestamp(){
		return this.mockTimestamp;
	}
}
