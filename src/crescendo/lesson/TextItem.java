package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * An item that contains just text.
 * @author forana
 */
public class TextItem implements PageItem
{
	// the text!
	private String text;
	
	/**
	 * @param text 
	 */
	public TextItem(String text)
	{
		this.text=text;
	}
	
	/**
	 * @param module The calling module.
	 * @return A JPanel representation of this item.
	 */
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		JPanel panel;
		panel = new TextPanel(text);
		return panel;
	}
}
