package crescendo.lesson;

import javax.swing.JPanel;

public class ImageItem implements PageItem
{
	private String source;
	private String alt;
	private String footer;
	
	public ImageItem(String source,String alt,String footer)
	{
		this.source=source;
		this.alt=alt;
		this.footer=footer;
	}
	
	public JPanel getPanel()
	{
		// TODO Barry
		return new JPanel();
	}
}
