package crescendo.sheetmusic.advicepattern;

import crescendo.base.ProcessedNoteEvent;
import crescendo.sheetmusic.AdvicePattern;
import crescendo.base.HeuristicsModel;
import crescendo.base.SongState;

/**
 * Provides a pattern that is triggered if a number of notes are played too early.
 * 
 * @author forana
 */
public class EarlyPattern implements AdvicePattern {
	private static final int MAX_TRANSGRESSIONS=3;
	private static final int TRIGGER_LEVEL=6;
	private static final String message="You're playing a little early.";
	
	private static final double THRESHOLD=0.3;
	
	// number that were early
	private int earlyCount;
	
	// number that weren't early
	private int transgressions;
	
	private HeuristicsModel heuristics;
	private SongState state;
	
	/**
	 * Creates a new pattern.
	 */
	public EarlyPattern(HeuristicsModel heuristics,SongState state) {
		this.earlyCount=0;
		this.transgressions=0;
		this.heuristics=heuristics;
		this.state=state;
	}
	
	/**
	 * Consider a new event.
	 * 
	 * @param event The event to consider.
	 */
	public void addEvent(ProcessedNoteEvent e) {
		// a missed/extra note isn't early
		if (e.getExpectedNote()==null || e.getPlayedNote()==null) {
			transgressions++;
		} else {
			int interval=(int)(this.heuristics.getTimingInterval()/(1.0*this.state.getBPM()/60/1000));
			if (e.getExpectedNote().getTimestamp()<=e.getPlayedNote().getTimestamp()
				&& Math.abs(e.getExpectedNote().getTimestamp()-e.getPlayedNote().getTimestamp())*1.0/interval>THRESHOLD) {
				transgressions++;
			} else {
				earlyCount++;
			}
		}
		// reset?
		if (transgressions>MAX_TRANSGRESSIONS) {
			earlyCount=0;
			transgressions=0;
		}
	}
	
	/**
	 * Determines if the pattern is currently matched.
	 * 
	 * @return true if the pattern is matched, false otherwise.
	 */
	public boolean matched() {
		return this.earlyCount>=TRIGGER_LEVEL;
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