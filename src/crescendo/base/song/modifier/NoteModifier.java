package crescendo.base.song.modifier;

import crescendo.base.SongState;
import crescendo.base.song.Note;
import java.util.List;
import java.util.LinkedList;

/**
 * Provides a generalization for a note modifier.
 */
public abstract class NoteModifier
{
	/** Call an arbitrary command. **/
	public void execute(SongState state)
	{
	}
	
	public List<Note> getNotes()
	{
		return new LinkedList<Note>();
	}
}
