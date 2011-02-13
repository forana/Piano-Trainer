package crescendo.lesson;

import javax.swing.JPanel;

public class TextItem implements PageItem
{
	private String text;
	
	public TextItem(String text)
	{
		this.text=text;
	}
	
	public JPanel getPanel()
	{
		return null;
	}
}
