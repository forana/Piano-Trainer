package crescendo.lesson;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import crescendo.base.HeuristicsModel;

/**
 * An item that contains a playable piece of sheet music.
 * @author forana
 */
public class MusicItem implements PageItem
{
	// filepath to the music item
	private String source;
	// relative filepath to the music item
	private String relSource;
	// heuristics used to grade lesson
	private HeuristicsModel heuristics;
	// grading scale used for lesson
	private GradingScale scale;
	// the index of the track to be played
	private int track;
	// data instance
	private LessonData data;
	
	/**
	 * @param source Filepath to the music file
	 * @param heuristics Heuristics model to be used for scoring individual notes
	 * @param scale Grading scale
	 * @param track Index of track to be played by user
	 * @param data Data instance by which to track scores.
	 */
	public MusicItem(String source,HeuristicsModel heuristics,GradingScale scale,int track,LessonData data)
	{
		this.source=source;
		this.relSource=this.source.substring(this.source.indexOf(".lesson/")+7);
		this.heuristics=heuristics;
		this.scale=scale;
		this.track=track;
	}
	
	/**
	 * A unique identifier for this item.
	 * For now this assumes that a single file will not appear multiple times.
	 * 
	 * @return
	 */
	public int getCode()
	{
		return this.relSource.hashCode();
	}
	
	/**
	 * @return The filepath to the music file.
	 */
	public String getSource()
	{
		return this.source;
	}
	
	/**
	 * @return The heuristics used for grading
	 */
	public HeuristicsModel getHeuristics()
	{
		return this.heuristics;
	}
	
	/**
	 * @return The grading scale used for this item.
	 */
	public GradingScale getScale()
	{
		return this.scale;
	}
	
	/**
	 * @return The index of the active track
	 */
	public int getTrack()
	{
		return this.track;
	}
	
	/**
	 * Set the data to store scores in.
	 * @param data The data instance
	 */
	public void linkLessonData(LessonData data)
	{
		this.data=data;
	}
	
	/**
	 * @return The data instance
	 */
	public LessonData getLessonData()
	{
		return this.data;
	}
	
	/**
	 * @param module The calling module.
	 * @return A JPanel representation of this item.
	 */
	public JPanel getPanel(JComponent module)
	{
		JPanel panel=new JPanel();
		panel.setBackground(Color.WHITE);
		panel.add(new JLabel("Failure loading music for \""+this.relSource+"\"."));
		try
		{
			panel=new MusicPanel(this,module);
		}
		catch (IOException e)
		{
		}
		return panel;
	}
}
