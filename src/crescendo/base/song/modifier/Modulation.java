package crescendo.base.song.modifier;

import crescendo.base.SongState;

public class Modulation extends NoteModifier {
	
	private int targetKeySignature;
	
	public Modulation(int targetKey){
		targetKeySignature = targetKey;
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
