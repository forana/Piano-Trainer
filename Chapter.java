package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

public class Chapter implements BookItem,LessonTreeNode
{
	private String title;
	private List<BookItem> contents;
	private List<PageItem> items;
	
	public Chapter(String title,List<BookItem> contents,List<PageItem> items)
	{
		this.title=title;
		this.contents=contents;
		this.items=items;
	}
	
	public List<BookItem> getContents()
	{
		return this.contents;
	}
	
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	public JPanel getPanel()
	{
		JPanel panel=new JPanel();
		panel.add(new JLabel("I am a chapter"));
		return panel;
	}
	
	public TreeNode getChildAt(int childIndex) {
		return this.contents.get(childIndex);
	}
	
	public boolean isLeaf() {
		return false;
	}
	
	public int getChildCount() {
		return this.contents.size();
	}
	
	public String toString() {
		return this.title;
	}

	public boolean getAllowsChildren() {return true;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}
