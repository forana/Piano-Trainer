package crescendo.base.song.modifier;

import crescendo.base.SongState;
import crescendo.base.song.TimeSignature;

public class TimeSignatureChange extends NoteModifier
{
	private TimeSignature time;
	
	public TimeSignatureChange(TimeSignature time)
	{
		this.time=time;
	}
	
	public void execute(SongState state)
	{
		state.setTimeSignature(time);
	}
	
	public TimeSignature getTargetTime()
	{
		return this.time;
	}
}
