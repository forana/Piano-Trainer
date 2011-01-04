package crescendo.base;

import crescendo.base.EventDispatcher.MidiEvent;
import crescendo.base.EventDispatcher.MidiEventListener;
import crescendo.base.EventDispatcher.ActionType;
import crescendo.base.song.Track;
import crescendo.base.song.Note;
import java.util.List;
import java.util.LinkedList;

public class SongValidator implements NoteEventListener,FlowController,MidiEventListener
{
	private static final int POOL_SIZE = 15; // theoretically more than 10 notes should ever happen
	
	private Track activeTrack;
	private ThreadPool pool;
	private int timeout;
	private List<ProcessedNoteEventListener> processedListeners;
	
	public SongValidator(Track activeTrack,int timeout)
	{
		this.activeTrack=activeTrack;
		this.timeout=timeout;
		this.pool=new ThreadPool(this,POOL_SIZE,this.timeout);
		this.processedListeners=new LinkedList<ProcessedNoteEventListener>();
	}
	
	public void handleNoteEvent(NoteEvent e)
	{
		if (e.getNote().getTrack()!=this.activeTrack)
		{
			boolean tryAgain=true;
			while (tryAgain)
			{
				Expirator free=this.pool.getAvailableExpirator();
				if (free==null) // if we couldn't get one, well, crap, we should figure out what to do
				{
					tryAgain=true;
				}
				else
				{
					try
					{
						free.expireNote(e);
						tryAgain=false;
					}
					catch (Expirator.ExpiratorBusyException ex)
					{
						tryAgain=true;
					}
				}
				if (tryAgain)
				{
					try
					{
						Thread.sleep(10);
					}
					catch (InterruptedException ex)
					{
					}
				}
			}
		}
	}
	
	public void pause()
	{
		this.pool.pause();
	}
	
	public void resume()
	{
		this.pool.resume();
	}
	
	public void songEnd()
	{
		this.pool.shutdown();
	}
	
	public void stop()
	{
		this.pool.shutdown();
		this.pool=new ThreadPool(this,POOL_SIZE,this.timeout);
	}
	
	public void suspend()
	{
		// technically we don't have to do anything here; pausing the pool doesn't do anything useful
	}
	
	public void attach(ProcessedNoteEventListener l)
	{
		this.processedListeners.add(l);
	}
	
	public void detach(ProcessedNoteEventListener l)
	{
		this.processedListeners.remove(l);
	}
	
	public void handleMidiEvent(MidiEvent midiEvent)
	{
		List<Expirator> busy=this.pool.getBusyExpirators();
		Expirator matched=null;
		int matchedScore=0;
		// score is a bit of trickery, may need some tuning
		//  pitch:
		//   exact:      +1000
		//   within 5:   + 500
		//  timing:
		//   distance:   + 750 - (|distance|/(timeout/2) * 750)
		//  velocity:
		//   not currently taken into account for score
		int aPitch=midiEvent.getNote();
		long aTime=midiEvent.getTimestamp();
		for (Expirator current : busy)
		{
			// don't match if the actions don't correspond
			if ((current.getNoteEvent().getAction()==NoteAction.BEGIN)
			     == (midiEvent.getAction()==ActionType.PRESS))
			{
				int currentScore=0;
				int ePitch=current.getNoteEvent().getNote().getPitch();
				long eTime=midiEvent.getTimestamp();
				
				if (ePitch==aPitch)
				{
					currentScore+=1000;
				}
				else if (Math.abs(ePitch-aPitch)<5)
				{
					currentScore+=500;
				}
				
				currentScore+=750-(int)(((int)Math.abs(eTime-aTime))/(this.timeout/2.0)*750);
				if (matched==null || currentScore>matchedScore)
				{
					matched=current;
					matchedScore=currentScore;
				}
			}
		}
		
		NoteEvent matchedEvent=null;
		NoteEvent playedEvent=new NoteEvent(
			new Note(midiEvent.getNote(),0,midiEvent.getVelocity(),this.activeTrack),
			(midiEvent.getAction()==ActionType.PRESS?NoteAction.BEGIN:NoteAction.END),
			midiEvent.getTimestamp());
		if (matched!=null)
		{
			matchedEvent=matched.getNoteEvent();
			matched.resolveNote();
		}
		ProcessedNoteEvent processed=new ProcessedNoteEvent(matchedEvent,playedEvent);
		for (ProcessedNoteEventListener listener : this.processedListeners)
		{
			listener.handleProcessedNoteEvent(processed);
		}
	}
	
	public void noteExpired(NoteEvent noteEvent)
	{
		ProcessedNoteEvent processed=new ProcessedNoteEvent(noteEvent,null);
		for (ProcessedNoteEventListener listener : this.processedListeners)
		{
			listener.handleProcessedNoteEvent(processed);
		}
	}
}
