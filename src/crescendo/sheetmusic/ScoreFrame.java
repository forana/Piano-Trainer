package crescendo.sheetmusic;

import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.ProcessedNoteEvent;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Provides a component to be displayed on the same screen as the songengine.
 * This component shows the number of notes that have been judged correct out
 * of all of the notes in the song, and an indicator of the current streak.
 * 
 * @author forana
 */
public class ScoreFrame extends JPanel implements ProcessedNoteEventListener {
	private static final long serialVersionUID=1L;
	
	// styling for labels
	private static final int BACKGROUND_COLOR=0x666666;
	private static final int TEXT_COLOR=0xFFFFFF;
	private static final Font FONT=new Font(Font.SANS_SERIF,Font.BOLD,16);
	
	// label for the % of notes that have been marked correct
	private JLabel scorePercentLabel;
	// label for the streak
	private JLabel streakLabel;
	// calculator that knows all about score
	private ScoreCalculator score;
	
	/**
	 * Creates a ScoreFrame with expectations based on a given track.
	 * 
	 * @param activeTrack The track currently being played.
	 */
	public ScoreFrame(ScoreCalculator calculator) {
		super();
		this.score=calculator;
		
		// assemble UI elements
		this.setBackground(new Color(BACKGROUND_COLOR));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.CENTER;
		c.fill=GridBagConstraints.NONE;
		c.weightx=1;
		c.weighty=1;
		JLabel t1=new JLabel("Percent Correct: ");
		t1.setFont(FONT);
		t1.setForeground(new Color(TEXT_COLOR));
		this.add(t1,c);
		this.scorePercentLabel=new JLabel("0.0");
		this.scorePercentLabel.setFont(FONT);
		this.scorePercentLabel.setForeground(new Color(TEXT_COLOR));
		this.add(this.scorePercentLabel,c);
		JLabel t2=new JLabel("%   Current Streak: ");
		t2.setFont(FONT);
		t2.setForeground(new Color(TEXT_COLOR));
		this.add(t2,c);
		this.streakLabel=new JLabel("0");
		this.streakLabel.setFont(FONT);
		this.streakLabel.setForeground(new Color(TEXT_COLOR));
		this.add(this.streakLabel,c);
	}
	
	/**
	 * Reset currently-calculated data.
	 */
	public void reset() {
		this.scorePercentLabel.setText("0.0");
		this.streakLabel.setText("0");
	}	
	
	/**
	 * Respond to a processed note being pumped out, altering the streak and potentially the 
	 */
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		this.score.handleProcessedNoteEvent(e);
		this.scorePercentLabel.setText(Double.toString(this.score.getCurrentPercent()));
		this.streakLabel.setText(Integer.toString(this.score.getStreak()));
		repaint();
	}
}
