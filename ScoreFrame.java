package crescendo.sheetmusic;

import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.ProcessedNoteEvent;
import crescendo.base.song.Track;
import crescendo.base.song.Note;
import crescendo.base.song.modifier.NoteModifier;
import crescendo.base.song.modifier.Chord;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class ScoreFrame extends JPanel implements ProcessedNoteEventListener
{
	private static final long serialVersionUID=1L;
	
	private static final int BACKGROUND_COLOR=0x666666;
	private static final int TEXT_COLOR=0xFFFFFF;
	private static final Font FONT=new Font(Font.SANS_SERIF,Font.BOLD,16);
	
	private JLabel scorePercentLabel;
	private JLabel streakLabel;
	private int expectedEventCount;
	private int correctCount;
	private int currentStreak;
	
	public ScoreFrame(Track activeTrack)
	{
		super();
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
		this.scorePercentLabel=new JLabel("0");
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
		
		// count notes
		this.expectedEventCount=0;
		this.correctCount=0;
		this.currentStreak=0;
		for (Note note : activeTrack.getNotes())
		{
			this.expectedEventCount+=2;
			for (NoteModifier modifier : note.getModifiers())
			{
				if (modifier instanceof Chord)
				{
					this.expectedEventCount+=2*modifier.getNotes().size();
				}
			}
		}
	}
	
	public void handleProcessedNoteEvent(ProcessedNoteEvent e)
	{
		if (e.isCorrect())
		{
			correctCount++;
			currentStreak++;
		}
		else
		{
			currentStreak=0;
		}
		this.scorePercentLabel.setText(Double.toString(1.0*correctCount/expectedEventCount));
		this.streakLabel.setText(Integer.toString(currentStreak));
		repaint();
	}
}
