package crescendo.base.song.modifier;

import crescendo.base.song.Note;
import java.util.List;
import java.util.LinkedList;

public class Tie implements NoteModifier
{
	private List<Note> notes;
	
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
	
	public List<Note> getNotes()
	{
		return this.notes;
	}
}
