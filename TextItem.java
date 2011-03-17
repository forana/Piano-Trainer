package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class TextItem implements PageItem
{
	private String text;
	
	public TextItem(String text)
	{
		this.text=text;
	}
	
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		JPanel panel;
		panel = new TextPanel(text);
		return panel;
	}
}
