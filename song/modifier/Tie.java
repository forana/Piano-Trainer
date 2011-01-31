package crescendo.base.song.modifier;

import crescendo.base.song.Note;

/**
 * Provides a modifier that allows one note to be tied to another without interrupting the relative structure of the song.
 * 
 * @author forana
 */
public class Tie extends NoteModifier
{
	/** The contained notes. */
	private Note start;
	private Note end;
	
	/**
	 * Creates a new tie, connecting two notes.
	 */
	public Tie(Note first,Note last)
	{
		this.start=first;
		this.end=last;
	}
	
	public Note getStartNote()
	{
		return this.start;
	}
	
	public Note getEndNote()
	{
		return this.end;
	}
}
