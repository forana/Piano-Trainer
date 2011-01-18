package crescendo.sheetmusic;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.LinkedList;

public class AdviceFrame extends JPanel implements ProcessedNoteEventListener
{
	private static final long serialVersionUID=1L;
	
	private static final int BACKGROUND_COLOR=0x666666;
	private static final int TEXT_COLOR=0xFFFFFF;
	private static final Font FONT=new Font(Font.SANS_SERIF,Font.BOLD,16);
	
	private JLabel label;
	
	private List<AdvicePattern> patterns;
	
	public AdviceFrame()
	{
		super();
		this.setBackground(new Color(BACKGROUND_COLOR));
		this.label=new JLabel("");
		this.label.setForeground(new Color(TEXT_COLOR));
		this.label.setFont(FONT);
		this.label.setHorizontalAlignment(JLabel.LEFT);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.anchor=GridBagConstraints.WEST;
		c.fill=GridBagConstraints.NONE;
		c.weightx=1;
		c.weighty=1;
		c.insets=new Insets(10,10,0,0);
		
		this.add(this.label,c);
		
		this.patterns=new LinkedList<AdvicePattern>();
		this.addPatterns();
	}
	
	private void addPatterns()
	{
		this.patterns.add(new EarlyNotesPattern());
	}
	
	public void handleProcessedNoteEvent(ProcessedNoteEvent e)
	{
		for (AdvicePattern pattern : this.patterns)
		{
			pattern.addEvent(e);
			if (pattern.matched())
			{
				this.setText(pattern.getMessage());
			}
		}
	}
	
	private void setText(String text)
	{
		this.label.setText(text);
		this.repaint();
	}
	
	private class EarlyNotesPattern implements AdvicePattern
	{
		private int earlyCount;
		private int transgressions;
		
		public EarlyNotesPattern()
		{
			this.earlyCount=0;
			this.transgressions=0;
		}
		
		public void addEvent(ProcessedNoteEvent e)
		{
			if (e.getExpectedNote()==null || e.getPlayedNote()==null)
			{
				transgressions++;
			}
			else
			{
				if (e.getExpectedNote().getTimestamp()<=e.getPlayedNote().getTimestamp())
				{
					transgressions++;
				}
				else
				{
					earlyCount++;
				}
			}
			if (transgressions>3)
			{
				earlyCount=0;
				transgressions=0;
			}
		}
		
		public boolean matched()
		{
			return this.earlyCount>=6;
		}
		
		public String getMessage()
		{
			return "You're playing a little early.";
		}
	}
}
