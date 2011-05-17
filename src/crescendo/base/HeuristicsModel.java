package crescendo.base;

import crescendo.base.EventDispatcher.MidiEvent;

/**
 * Provides a class that can judge whether or not a note is correct based on
 * a few parameters.
 * 
 * @author forana
 */
public class HeuristicsModel {
	/**
	 * Default amount of time to allow for note expiration (in beats).
	 */
	public static double DEFAULT_TIMING_INTERVAL=0.5;
	
	/**
	 * Default tolerance for velocity.
	 */
	public static int DEFAULT_VELOCITY_TOLERANCE=30;
	
	// Timing interval in beats
	private double timingInterval;
	
	// Amount the velocity is allowed to be off
	private int velocityTolerance;
	
	// Actually care about pitch?
	private boolean listenPitch;
	
	// Actually care about velocity?
	private boolean listenVelocity;
	
	/**
	 * Create a HeuristicsModel with default values.
	 * 
	 * @param listenPitch Whether or not to care about pitch differences.
	 * @param listenVelocity Whether or note to care about velocity differences.
	 */
	public HeuristicsModel(boolean listenPitch,boolean listenVelocity) {
		this.timingInterval=DEFAULT_TIMING_INTERVAL;
		this.velocityTolerance=DEFAULT_VELOCITY_TOLERANCE;
		this.listenPitch=listenPitch;
		this.listenVelocity=listenVelocity;
	}
	
	/**
	 * Creates a HeuristicModel.
	 * 
	 * @param timingInterval The amount of beats to allow in either direction.
	 * @param velocityTolerance The amount of change to allow in velocity and still be correct.
	 * @param listenPitch Whether or not to care about pitch differences.
	 * @param listenVelocity Whether or note to care about velocity differences.
	 */
	public HeuristicsModel(double timingInterval,int velocityTolerance,boolean listenPitch,boolean listenVelocity) {
		this.timingInterval=timingInterval;
		this.velocityTolerance=velocityTolerance;
		this.listenPitch=listenPitch;
		this.listenVelocity=listenVelocity;
	}
	
	/**
	 * Get the timing interval, in beats.
	 * 
	 * @return The number of beats away a note can be and still be correct.
	 */
	public double getTimingInterval() {
		return this.timingInterval;
	}
	
	public boolean listeningPitch() {
		return this.listenPitch;
	}
	
	public boolean listeningVelocity() {
		return this.listenVelocity;
	}
	
	/**
	 * Judge whether or not a note pairing is correct.
	 * 
	 * @param expected The expected note.
	 * @param played The note that was played.
	 * 
	 * @return true if the note is judged correct, false otherwise.
	 */
	public boolean judge(NoteEvent expected,MidiEvent played) {
		if (expected==null || played==null) {
			return false;
		}
		// TODO explain this here
		return (!this.listenPitch || (expected.getNote().getPitch()==played.getNote())) &&
			(!this.listenVelocity || (Math.abs(expected.getNote().getDynamic()-played.getVelocity())<=this.velocityTolerance));
	}
}
