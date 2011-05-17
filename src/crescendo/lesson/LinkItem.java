package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * An item with a traditional http link on it.
 * @author forana
 */
public class LinkItem implements PageItem
{
	// link target
	private String url;
	// link text
	private String text;
	
	/**
	 * @param url The target URL
	 * @param text The link text
	 */
	public LinkItem(String url,String text)
	{
		this.url=url;
		this.text=text;
	}
	
	/**
	 * @param module The calling module.
	 * @return A JPanel representation of this item.
	 */
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		return new LinkPanel(text,url);
	}
}
