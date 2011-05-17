package crescendo.base.song;

/**
 * Data structure for holding the time signature of a song
 * @author nickgartmann
 *
 */
public class TimeSignature {
	private double beatsPerMeasure;
	private double beatNote;

	/**
	 * Constructor
	 * set the beats per measure and the note which gets the beat 
	 * @param beatsPerMeasure the number of beats which exist in a measure
	 * @param beatNote the type of note which has the value of one beat (4 for a quarter note, 8 for an eighth note, etc)
	 */
	public TimeSignature(double beatsPerMeasure, double beatNote) {
		this.beatsPerMeasure = beatsPerMeasure;
		this.beatNote = beatNote;
	}
	
	public double getBeatsPerMeasure() {
		return beatsPerMeasure;
	}

	public double getBeatNote() {
		return beatNote;
	}
}
