package crescendo.lesson;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BoxLayout;
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
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		JPanel header=new JPanel();
		header.setLayout(new FlowLayout(FlowLayout.CENTER));
		header.setBackground(Color.WHITE);
		JLabel titleLabel=new JLabel(this.title);
		titleLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
		header.add(titleLabel);
		panel.add(header);
		for (PageItem item : this.items)
		{
			panel.add(item.getPanel());
		}
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
