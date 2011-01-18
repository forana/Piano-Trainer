package crescendo.sheetmusic;

import crescendo.base.ProcessedNoteEvent;

public interface AdvicePattern
{
	public void addEvent(ProcessedNoteEvent event);
	
	public boolean matched();
	
	public String getMessage();
}
