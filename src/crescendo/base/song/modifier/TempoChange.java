package crescendo.base.song.modifier;

import crescendo.base.SongState;

public class TempoChange extends NoteModifier
{
	private int tempo;
	
	public TempoChange(int tempo)
	{
		this.tempo=tempo;
	}
	
	public void execute(SongState state)
	{
		state.setBPM(tempo);
	}
	
	public int getTargetTempo()
	{
		return this.tempo;
	}
}
