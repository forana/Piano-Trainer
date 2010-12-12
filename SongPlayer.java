package crescendo.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import crescendo.base.song.SongModel;

/**
 * A SongPlayer does the actual running through the song and propagation of events.
 * 
 * @author forana
 */
public class SongPlayer
{
	// TODO Pretty much all of this class

	/**
	 * The songed that is being played.
	 */
	private SongModel songModel;
	/** map of listeners and the number of milliseconds early to send their note events */
	private Map<NoteEventListener,Integer> listeners;
	/** list of notes which are currently in the queue */
	private List<NoteEvent> activeNotes;
	/**
	 * TODO This
	 * 
	 * @param songModel
	 */
	public SongPlayer(SongModel songModel)
	{
		this.songModel=songModel;

		listeners = Collections.synchronizedMap(new HashMap<NoteEventListener,Integer>());
		activeNotes = new ArrayList<NoteEvent>();
	}

	/**
	 * TODO This
	 */
	public SongModel getSong()
	{
		return this.songModel;
	}

	/**
	 * Subscribe to receive note events from the song player
	 * @param listener instance of NoteEventListener to pump events to
	 * @param time number of milliseconds in advance the listener wants to receive note events
	 */	
	public void attach(NoteEventListener listener, int time) {
		listeners.put(listener, time);
	}

	/**
	 * Remove listener from the listeners who are receiving note events from the player.
	 * @param listener - NoteEventLIstener instance to remove from the observer list
	 */
	public void detach(NoteEventListener listener) {
		listeners.remove(listener);
	}


	/**
	 * Sends out notes to the listeners. Only sends them out if it is
	 * within the requested time frame. This method also removes old notes 
	 * from the list of active notes.
	 */
	private void update() {
		long now = System.currentTimeMillis();
		NoteEvent event = null;
		for(Iterator<NoteEvent> i = activeNotes.iterator(); i.hasNext();) {
			event = i.next();
			if(now > event.getTimestamp())
			{
				i.remove();
				continue;
			}
			for(NoteEventListener listener : listeners.keySet()) {
				if(now >= (event.getTimestamp()-listeners.get(listener))) {
					listener.handleNoteEvent(event);
				}
			}
		}
	}
}
