package crescendo.sheetmusic;

import crescendo.base.HeuristicsModel;
import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.SongState;
import crescendo.sheetmusic.advicepattern.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.LinkedList;

/**
 * Provides a component that conditionally provides advice for a user to
 * improve their piano playing with.
 * 
 * @author forana
 */
public class AdviceFrame extends JPanel implements ProcessedNoteEventListener {
	private static final long serialVersionUID=1L;
	
	// UI styling
	private static final int BACKGROUND_COLOR=0x666666;
	private static final int TEXT_COLOR=0xFFFFFF;
	private static final Font FONT=new Font(Font.SANS_SERIF,Font.BOLD,16);
	
	// label for message
	private JLabel label;
	
	// list of patterns being used
	private List<AdvicePattern> patterns;
	
	private HeuristicsModel heuristics;
	private SongState state;
	
	/**
	 * Creates a new AdviceFrame.
	 */
	public AdviceFrame(HeuristicsModel heuristics,SongState state) {
		super();
		
		this.heuristics=heuristics;
		this.state=state;
		
		// UI elements
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
		
		// build pattern list
		this.buildPatterns();
	}
	
	/**
	 * Fills the pattern list.
	 */
	private void buildPatterns() {
		this.patterns=new LinkedList<AdvicePattern>();
		this.patterns.add(new QuietPattern());
		this.patterns.add(new LoudPattern());
		this.patterns.add(new EarlyPattern(heuristics,state));
		this.patterns.add(new LatePattern(heuristics,state));
	}
	
	/**
	 * Reset all currently-counting patterns.
	 */
	public void reset() {
		this.buildPatterns();
	}
	
	/**
	 * Pass the event to all patterns being considered.
	 */
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		for (AdvicePattern pattern : this.patterns) {
			pattern.addEvent(e);
			if (pattern.matched()) {
				this.setText(pattern.getMessage());
			}
		}
	}
	
	/**
	 * Set the text of the label and repaint.
	 * 
	 * @param text The new text.
	 */
	private void setText(String text) {
		this.label.setText(text);
		this.repaint();
	}
}
