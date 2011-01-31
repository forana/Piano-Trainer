package crescendo.base.song.modifier;

import java.util.ArrayList;
import java.util.List;
import crescendo.base.SongState;

import crescendo.base.song.Note;

public class Modulation extends NoteModifier {
	
	private int targetKeySignature;
	private List<Note> note;
	
	public Modulation(Note note,int targetKey){
		targetKeySignature = targetKey;
		this.note = new ArrayList<Note>();
		this.note.add(note);
	}
	
	@Override
	public void execute(SongState state) {
		state.setKey(this.targetKeySignature);
	}
	
	public int getTargetKey()
	{
		return this.targetKeySignature;
	}
}
