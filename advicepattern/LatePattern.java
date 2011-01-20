package crescendo.sheetmusic.advicepattern;

import crescendo.base.ProcessedNoteEvent;
import crescendo.sheetmusic.AdvicePattern;

/**
 * Provides a pattern that is triggered if a number of notes are played too late.
 * 
 * @author forana
 */
public class LatePattern implements AdvicePattern {
	private static final int MAX_TRANSGRESSIONS=3;
	private static final int TRIGGER_LEVEL=6;
	private static final String message="You're playing a little late.";
	
	// number that were late
	private int lateCount;
	
	// number that weren't late
	private int transgressions;
	
	/**
	 * Creates a new pattern.
	 */
	public LatePattern() {
		this.lateCount=0;
		this.transgressions=0;
	}
	
	/**
	 * Consider a new event.
	 * 
	 * @param event The event to consider.
	 */
	public void addEvent(ProcessedNoteEvent e) {
		// a missed/extra note isn't late
		if (e.getExpectedNote()==null || e.getPlayedNote()==null) {
			transgressions++;
		} else if (e.getExpectedNote().getTimestamp()>=e.getPlayedNote().getTimestamp()) {
			transgressions++;
		} else {
			lateCount++;
		}
		// reset?
		if (transgressions>MAX_TRANSGRESSIONS) {
			lateCount=0;
			transgressions=0;
		}
	}
	
	/**
	 * Determines if the pattern is currently matched.
	 * 
	 * @return true if the pattern is matched, false otherwise.
	 */
	public boolean matched() {
		return this.lateCount>=TRIGGER_LEVEL;
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