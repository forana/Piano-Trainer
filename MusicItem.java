package crescendo.lesson;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import crescendo.base.HeuristicsModel;

public class MusicItem implements PageItem
{
	private String source;
	private String relSource;
	private HeuristicsModel heuristics;
	private GradingScale scale;
	private int track;
	private LessonData data;
	
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
	
	public String getSource()
	{
		return this.source;
	}
	
	public HeuristicsModel getHeuristics()
	{
		return this.heuristics;
	}
	
	public GradingScale getScale()
	{
		return this.scale;
	}
	
	public int getTrack()
	{
		return this.track;
	}
	
	public void linkLessonData(LessonData data)
	{
		this.data=data;
	}
	
	public LessonData getLessonData()
	{
		return this.data;
	}
	
	public JPanel getPanel()
	{
		JPanel panel=new JPanel();
		panel.setBackground(Color.WHITE);
		panel.add(new JLabel("Failure loading music for \""+this.relSource+"\"."));
		try
		{
			panel=new MusicPanel(this);
		}
		catch (IOException e)
		{
		}
		return panel;
	}
}
