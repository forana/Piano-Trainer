package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class LinkItem implements PageItem
{
	private String url;
	private String text;
	
	public LinkItem(String url,String text)
	{
		this.url=url;
		this.text=text;
	}
	
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		// TODO Barry
		return new JPanel();
	}
}
