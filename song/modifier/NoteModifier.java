package crescendo.base.song.modifier;

import crescendo.base.song.Note;
import java.util.List;

/**
 * Provides a generalization for a note modifier.
 */
public interface NoteModifier
{
	/** Call an arbitrary command. **/
	public void execute();
	
	/** Return the notes associated with this modifier. **/
	public List<Note> getNotes();
}
