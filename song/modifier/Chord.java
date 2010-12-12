package crescendo.base.song.modifier;

import java.util.List;
import crescendo.base.song.Note;

public class Chord implements NoteModifier
{
	private List<Note> notes;
	
	public Chord(List<Note> notes)
	{
		this.notes=notes;
	}
	
	public void execute()
	{
		//TODO Figure out wtf to do here
	}
	
	public List<Note> getNotes()
	{
		return this.notes;
	}
}
