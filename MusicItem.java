package crescendo.lesson;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import crescendo.base.HeuristicsModel;

public class MusicItem implements PageItem
{
	private String source;
	private HeuristicsModel heuristics;
	private GradingScale scale;
	private LessonData data;
	
	public MusicItem(String source,HeuristicsModel heuristics,GradingScale scale,LessonData data)
	{
		this.source=source;
		this.heuristics=heuristics;
		this.scale=scale;
	}
	
	/**
	 * A unique identifier for this item.
	 * For now this assumes that a single file will not appear multiple times.
	 * 
	 * @return
	 */
	public int getCode()
	{
		return this.source.hashCode();
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
		panel.add(new JLabel("Placeholder MusicItem"));
		return panel;
	}
}
