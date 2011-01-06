package crescendo.base.song.modifier;

import java.util.ArrayList;
import java.util.List;

import crescendo.base.song.Note;

public class Modulation implements NoteModifier {
	
	public int targetKeySignature; //This should be public so it can be accessed after casting the NoteModifier to a Modulation modifier.
	private List<Note> note;
	
	public Modulation(Note note,int targetKey){
		targetKeySignature = targetKey;
		this.note = new ArrayList<Note>();
		this.note.add(note);
	}
	
	@Override
	public void execute() {
		//This method does nothing for the modulation modifier, it is for markup purposes only
	}

	@Override
	public List<Note> getNotes() {
		return note;
	}

}
