package crescendo.lesson;

import javax.swing.JPanel;
import crescendo.base.HeuristicsModel;

public class MusicItem implements PageItem
{
	private String source;
	private HeuristicsModel heuristics;
	private GradingScale scale;
	
	public MusicItem(String source,HeuristicsModel heuristics,GradingScale scale)
	{
		this.source=source;
		this.heuristics=heuristics;
		this.scale=scale;
	}
	
	public int getCode()
	{
		return 4;
	}
	
	public JPanel getPanel()
	{
		return null;
	}
}
