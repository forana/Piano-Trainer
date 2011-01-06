package crescendo.base;

import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import crescendo.base.song.SongModel;

/**
 * A SongPlayer does the actual running through the song and propagation of events.
 * 
 * @author forana
 * @author gartmannn
 */
public class SongPlayer
{


	/** The song that is being played.*/
	private SongModel songModel;
	/** map of listeners and the number of milliseconds early to send their note events */
	private Map<NoteEventListener,Integer> listeners;
	/** list of notes which are currently in the queue */
	private Map<NoteEvent,List<NoteEventListener>> activeNotes;

	/** list of flow controllers (objects that get told about flow events such as pause and resume) */
	private Set<FlowController> controllers;
	
	
	/** Runnable which does timing */
	private PlayerTimer timer;
	/** Thread which holds our timer runnable */
	private Thread timerContainer;

	private boolean isPaused;
	private boolean doContinue;

	/** The number of milliseconds the song has been paused */
	private long pauseOffset;

	/**
	 * Create a song player for the given song model. 
	 * Initializes the lists of controllers and listeners, and creates the timer
	 * There should be a 1 to 1 relationship between song players and song models
	 * @param songModel song model representing the song we wish to play.
	 */
	public SongPlayer(SongModel songModel)
	{
		this.songModel=songModel;
		listeners = Collections.synchronizedMap(new HashMap<NoteEventListener,Integer>());
		controllers = Collections.synchronizedSet(new HashSet<FlowController>());
		timer = new PlayerTimer();
		timerContainer = new Thread(timer);
	}

	/**
	 * Begin playing the song
	 * This resets all of the state variables, <b>DO NOT</b> resume the song after pausing, use the resume function. 
	 */
	public void play() {
		activeNotes = new HashMap<NoteEvent,List<NoteEventListener>>();
		isPaused = false;
		doContinue = true;
		pauseOffset = 0;
		timerContainer.start();
		//get a new iterator from the song model
	}

	public void pause() {
		isPaused = true;
		for(FlowController controller : controllers) {
			controller.pause();
		}
	}

	public void resume() {
		isPaused = false;
		for(NoteEvent event : activeNotes.keySet()) {
			event.setTimestamp(event.getTimestamp()+pauseOffset);
		}
		pauseOffset = 0;
		for(FlowController controller : controllers) {
			controller.resume();
		}
	}

	public void stop() {
		for(FlowController controller : controllers) {
			controller.stop();
		}
		doContinue=false;
	}

	/**
	 * Get the song model for this song
	 * @return the song model that this player is playing
	 */
	public SongModel getSong()
	{
		return this.songModel;
	}

	/** 
	 * Subscribe to receive flow control calls from the song player
	 * @param controller instance of FlowController to call on
	 */
	public void attach(FlowController controller) {
		controllers.add(controller);
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
	 * Remove controller from the controllers who are receiving control calls from the player
	 * @param controller instance of FlowController to remove from the list
	 */
	public void detach(FlowController controller) {
		controllers.remove(controller);
	}

	/**
	 * Sends out notes to the listeners. Only sends them out if it is
	 * within the requested time frame. This method also removes old notes 
	 * from the list of active notes.
	 */
	private void update() {
		long now = System.currentTimeMillis();




		//Pump out note events
		NoteEvent event = null;
		for(Iterator<NoteEvent> i = activeNotes.keySet().iterator(); i.hasNext();) {
			event = i.next();
			if(now > event.getTimestamp())
			{
				i.remove(); //This removes the note from the map
			}else{
				for(NoteEventListener listener : listeners.keySet()) {
					//Only send the note if it is within the correct range, and the listener has not received it yet
					if(now >= (event.getTimestamp()-listeners.get(listener))
							&& !activeNotes.get(event).contains(listener)) {
						listener.handleNoteEvent(event);
						activeNotes.get(event).add(listener);
					}
				}
			}
		}
	}

	/**
	 * Runnable class which calls update FRAMES_PER_SECOND times per second
	 * or adds to the pause offset if the song is paused.
	 * @author nickgartmann
	 */
	private class PlayerTimer implements Runnable {
		private final int FRAMES_PER_SECOND = 100;
		/** number of milliseconds from the epoch of when the last frame started */
		private long lastFrame = 0;

		/**
		 * Method which get called by the thread, this is running while the song is playing or paused
		 */
		@Override
		public void run() {
			while(doContinue) {
				long now = System.currentTimeMillis();
				if(!isPaused) {
					if(now > (lastFrame + (1000.0/(float)FRAMES_PER_SECOND))) {
						update();			
					}else{
						try {
							Thread.sleep(1); // Dont eat up all the processor
						} 
						catch (InterruptedException e) {}
					}
				}else{
					pauseOffset+=(now-lastFrame);
				}
				lastFrame = now;	//We want to run at FRAMES_PER_SECOND fps, so use the beginning of the frame to
									//ensure that we get the correct frames, no matter how long update takes
			}
		}
	}
}
