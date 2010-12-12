package crescendo.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Map<NoteEventListener,Integer> listeners;
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

	public void detach(NoteEventListener listener) {
		listeners.remove(listener);
	}


	private void update() {
		long now = System.currentTimeMillis();
		for(NoteEvent event : activeNotes) {
			for(NoteEventListener listener : listeners.keySet()) {
				if(now >= (event.getTimestamp()-listeners.get(listener))) {
					listener.handleNoteEvent(event);
				}
			}
		}
	}
}
