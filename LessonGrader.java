package crescendo.lesson;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;

/**
 * Tallies totals for lesson grading.
 * 
 * @author forana
 * @version 1.0
 * @created 20-Feb-2011 2:00:09 PM
 */
public class LessonGrader implements ProcessedNoteEventListener {
	private int countTotal;
	private int countCorrect;

	public LessonGrader(){
		this.reset();
	}
	
	public void reset(){
		this.countTotal=0;
		this.countCorrect=0;
	}

	@Override
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		this.countTotal++;
		if (e.isCorrect()) {
			this.countCorrect++;
		}
	}
	
	public double getScore() {
		return 100.0*countCorrect/countTotal;
	}
}