package crescendo.sheetmusic;

import crescendo.base.ProcessedNoteEvent;

/**
 * Provides an interface to create patterns to be recognized and handled by the
 * AdviceFrame.
 * 
 * @author forana
 */
public interface AdvicePattern {
	/**
	 * Consider a new event.
	 * 
	 * @param event The event to consider.
	 */
	public void addEvent(ProcessedNoteEvent event);
	
	/**
	 * Determines if the pattern is currently matched.
	 * 
	 * @return true if the pattern is matched, false otherwise.
	 */
	public boolean matched();
	
	/**
	 * The message that should be displayed if the pattern is matched.
	 * 
	 * @return the message.
	 */
	public String getMessage();
}
