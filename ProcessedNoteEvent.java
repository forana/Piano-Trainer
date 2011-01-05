package crescendo.base;

import crescendo.base.EventDispatcher.MidiEvent;

public class ProcessedNoteEvent
{
	private MidiEvent playedNote;
	private NoteEvent expectedNote;
	
	public ProcessedNoteEvent(NoteEvent expectedNote,MidiEvent playedNote)
	{
		this.playedNote=playedNote;
		this.expectedNote=expectedNote;
	}
	
	public MidiEvent getPlayedNote()
	{
		return this.playedNote;
	}
	
	public NoteEvent getExpectedNote()
	{
		return this.expectedNote;
	}
}
