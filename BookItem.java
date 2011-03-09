package crescendo.lesson;

import java.util.List;

import javax.swing.tree.TreeNode;

public interface BookItem extends LessonTreeNode
{
	public List<PageItem> getItems();
}
