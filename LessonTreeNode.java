package crescendo.lesson;

import javax.swing.tree.TreeNode;
import javax.swing.JComponent;
import javax.swing.JPanel;

public interface LessonTreeNode extends TreeNode
{
	public JPanel getPanel(JComponent module);
}
