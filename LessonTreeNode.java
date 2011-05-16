package crescendo.lesson;

import javax.swing.tree.TreeNode;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Represents a viewable node in a lesson tree.
 * @author forana
 */
public interface LessonTreeNode extends TreeNode
{
	/**
	 * @param module The calling module.
	 * @return An instance of a JPanel to show for this node.
	 */
	public JPanel getPanel(JComponent module);
}
