package crescendo.base;

/**
 * Provides a generalization for objects that wish to interact with ProcessedNoteEvents.
 * 
 * @author forana
 */
public interface ProcessedNoteEventListener
{
	/**
	 * Does something with a ProcessedNoteEvent.
	 * 
	 * @param e The event.
	 */
	public void handleProcessedNoteEvent(ProcessedNoteEvent e);
}
