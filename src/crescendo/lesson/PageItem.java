package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Represents an item that can be shown on a page.
 * @author forana
 */
public interface PageItem
{
	/**
	 * @param module The calling module.
	 * @return A JPanel representation of this item.
	 */
	public JPanel getPanel(JComponent module);
}