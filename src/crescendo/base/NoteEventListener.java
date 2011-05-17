package crescendo.base;

/**
 * A NoteEventListener is an object that takes in NoteEvents and does something with them.
 * 
 * @author forana
 */
public interface NoteEventListener
{
	/**
	 * Perform an action using a NoteEvent.
	 * 
	 * @param e The NoteEvent.
	 */
	public void handleNoteEvent(NoteEvent e);
}
