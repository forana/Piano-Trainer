package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JLabel;
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
	
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		JPanel panel = new JPanel();
		panel.add(new JLabel("Error Loading Image"));
		try
		{
		panel = new ImagePanel(source);
		}catch(Exception e)
		{	
		}
		return panel;
	}
}
