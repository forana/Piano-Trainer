package crescendo.base.song.modifier;

import java.util.List;
import crescendo.base.song.Note;

/**
 * Provides a structure to store chorded notes without interrupting the relative flow of the song.
 */
public class Chord implements NoteModifier
{
	/**
	 * The notes that this chord contains.
	 */
	private List<Note> notes;
	
	/**
	 * Creates a new Chord comprising several notes.
	 * 
	 * @param notes The notes comprised.
	 */
	public Chord(List<Note> notes)
	{
		this.notes=notes;
	}
	
	public void execute()
	{
		//TODO Figure out wtf to do here
	}
	
	/**
	 * Gets the notes represented by this chord.
	 * 
	 * @return The notes.
	 */
	public List<Note> getNotes()
	{
		return this.notes;
	}
}
