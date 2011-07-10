package crescendo.tester;

import java.util.List;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.ActionType;
import crescendo.base.EventDispatcher.MidiEvent;
import crescendo.base.EventDispatcher.MidiEventListener;


public class MockTransmitter implements Transmitter,NoteEventListener {
	//debug 4 pitch 4 8 pitch 4 12 pitch 4 16 pitch 4 20 pitch 4 24 pitch 4 28 pitch 4 32 pitch 4 36 pitch 4 40 pitch 4 44 pitch 4 48 pitch 4 52 pitch 4 56 pitch 4 60 pitch 4 64 pitch 4
	private Receiver receiver;
	private static MockTransmitter instance;
	private static boolean isCreated = false;
	private int count;
	private List<Integer> indexes;
	private List<String> variable;
	private List<Integer> amounts;
	private SongValidator validator;
	private int reset;


	private MockTransmitter(){
		count = 0;
		reset=0;
		isCreated = true;

	}

	public static MockTransmitter getInstance(){

		if(!isCreated){
			instance = new MockTransmitter();
		}
		return instance;


	}

	@Override
	public void close() {
		receiver.close();
		System.out.println("warning");

	}

	@Override
	public Receiver getReceiver() {
		return receiver;
	}

	@Override
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;

	}

	@Override
	public void handleNoteEvent(NoteEvent e) {
		if(e.getAction()==NoteAction.BEGIN){
			count++;
			System.out.println("Count:"+count);
		}
		MockMidiEvent message;

		if(indexes.contains(count)){
			String modifier = variable.get(indexes.indexOf(count));
			if(e.getAction()==NoteAction.BEGIN){
				reset++;
			}
			if(modifier.equals("pitch")){
				message = new MockMidiEvent(e.getNote().getPitch()+amounts.get(indexes.indexOf(count)), 
						e.getNote().getDynamic(),
						(e.getAction()==NoteAction.BEGIN?ActionType.PRESS:ActionType.RELEASE));
				message.setTimestamp(e.getTimestamp());
				System.out.println("one");
			}else{
				message = new MockMidiEvent(e.getNote().getPitch(), e.getNote().getDynamic(),(e.getAction()==NoteAction.BEGIN?ActionType.PRESS:ActionType.RELEASE));
				message.setTimestamp(e.getTimestamp());
				System.out.println("Two");
			}
			
		}
		else
		{
			message = new MockMidiEvent(e.getNote().getPitch(), e.getNote().getDynamic(),(e.getAction()==NoteAction.BEGIN?ActionType.PRESS:ActionType.RELEASE));
			message.setTimestamp(e.getTimestamp());
			System.out.println("Three");
		}
		System.out.println(message.getNote()+":Note");
		this.validator.handleMidiEvent(message);
		
		if(count == 64){
			count=0;
		}

	}

	public void setIndexes(List<Integer> ind){
		indexes = ind;
	}

	public void setVariables(List<String> vars){
		variable = vars;
	}

	public void setAmounts(List<Integer> amts){
		amounts = amts;
	}
	
	public void setValidator(SongValidator validator) {
		this.validator = validator;
	}
	
	/**
	 * 
	 * @author larkinp
	 * No longer needed, used in an old implementation. May be used in the future
	 */
	private class MockMessage extends MidiMessage{

		protected MockMessage(byte[] data) {
			super(data);	
		}

		@Override
		public Object clone() {
			return this;
		}

	}

	



}
