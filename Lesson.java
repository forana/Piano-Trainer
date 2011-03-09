package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

public class Lesson implements BookItem,LessonTreeNode
{
	private String title;
	private List<PageItem> items;
	
	public Lesson(String title,List<PageItem> items)
	{
		this.title=title;
		this.items=items;
	}
	
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	public void loadChildren()
	{
	}
	
	public JPanel getPanel()
	{
		JPanel panel=new JPanel();
		panel.add(new JLabel("I am a lesson"));
		return panel;
	}
	
	public String toString()
	{
		return this.title;
	}
	
	public TreeNode getChildAt(int childIndex) {
		return null;
	}
	
	public boolean isLeaf() {
		return true;
	}
	
	public int getChildCount() {
		return 0;
	}

	public boolean getAllowsChildren() {return false;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}
