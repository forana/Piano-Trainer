package crescendo.sheetmusic.advicepattern;

import crescendo.base.ProcessedNoteEvent;
import crescendo.sheetmusic.AdvicePattern;

/**
 * Provides a pattern that is triggered if a number of notes are played too early.
 * 
 * @author forana
 */
public class QuietPattern implements AdvicePattern {
	private static final int MAX_TRANSGRESSIONS=3;
	private static final int TRIGGER_LEVEL=6;
	private static final int LEVEL_THRESHOLD=30;
	private static final String message="You're too quiet! Play a bit harder.";
	
	// number that were quiet
	private int quietCount;
	
	// number that weren't early
	private int transgressions;
	
	/**
	 * Creates a new pattern.
	 */
	public QuietPattern() {
		this.quietCount=0;
		this.transgressions=0;
	}
	
	/**
	 * Consider a new event.
	 * 
	 * @param event The event to consider.
	 */
	public void addEvent(ProcessedNoteEvent e) {
		// a missed/extra note isn't quiet
		if (e.getExpectedNote()==null || e.getPlayedNote()==null) {
			transgressions++;
		} else if (e.getExpectedNote().getNote().getDynamic()-e.getPlayedNote().getVelocity()<LEVEL_THRESHOLD) {
			transgressions++;
		} else {
			quietCount++;
		}
		// reset?
		if (transgressions>MAX_TRANSGRESSIONS) {
			quietCount=0;
			transgressions=0;
		}
	}
	
	/**
	 * Determines if the pattern is currently matched.
	 * 
	 * @return true if the pattern is matched, false otherwise.
	 */
	public boolean matched() {
		return this.quietCount>=TRIGGER_LEVEL;
	}
	
	/**
	 * The message that should be displayed if the pattern is matched.
	 * 
	 * @return the message.
	 */
	public String getMessage() {
		return message;
	}
}