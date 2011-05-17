package crescendo.base;

import crescendo.base.song.TimeSignature;

public class SongState
{
	private int bpm;
	private TimeSignature time;
	private int key;
	
	public SongState(int initialBPM,TimeSignature initialSignature,int initialKey)
	{
		this.bpm=initialBPM;
		this.time=initialSignature;
		this.key=initialKey;
	}
	
	public int getBPM()
	{
		return this.bpm;
	}
	
	public TimeSignature getTimeSignature()
	{
		return this.time;
	}
	
	public int getKeySignature()
	{
		return this.key;
	}
	
	public void setBPM(int newBPM)
	{
		this.bpm=newBPM;
	}
	
	public void setTimeSignature(TimeSignature newTime)
	{
		this.time=newTime;
	}
	
	public void setKey(int newKey)
	{
		this.key=newKey;
	}
}
