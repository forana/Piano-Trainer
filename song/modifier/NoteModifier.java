package crescendo.base.song.modifier;

import crescendo.base.song.Note;
import java.util.List;

public interface NoteModifier
{
	public void execute();
	public List<Note> getNotes();
}
