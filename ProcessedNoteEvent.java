package crescendo.base;

public class ProcessedNoteEvent
{
	private NoteEvent playedNote;
	private NoteEvent expectedNote;
	
	public ProcessedNoteEvent(NoteEvent expectedNote,NoteEvent playedNote)
	{
		this.playedNote=playedNote;
		this.expectedNote=expectedNote;
	}
	
	public NoteEvent getPlayedNote()
	{
		return this.playedNote;
	}
	
	public NoteEvent getExpectedNote()
	{
		return this.expectedNote;
	}
}
