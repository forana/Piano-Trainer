package crescendo.base.song.modifier;

import crescendo.base.song.Note;
import java.util.List;
import java.util.LinkedList;

/**
 * Provides a modifier that allows one note to be tied to another without interrupting the relative structure of the song.
 * 
 * @author forana
 */
public class Tie implements NoteModifier
{
	/** The contained notes. */
	private List<Note> notes;
	
	/**
	 * Creates a new tie, connecting two notes.
	 */
	public Tie(Note first,Note last)
	{
		this.notes=new LinkedList<Note>();
		this.notes.add(first);
		this.notes.add(last);
	}
	
	public void execute()
	{
		//TODO This
	}
	
	/**
	 * Gets the notes represented by this tie.
	 * 
	 * @return The notes.
	 */
	public List<Note> getNotes()
	{
		return this.notes;
	}
}
