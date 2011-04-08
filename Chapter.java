package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
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
	
	public String getTitle()
	{
		return this.title;
	}
	
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	public JPanel getPanel(JComponent module)
	{
		return new ChapterPanel(this,module);
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
		String t=this.title;
		int count=0;
		int complete=0;
		double total=0;
		for (BookItem item : this.contents)
		{
			if (item instanceof Lesson)
			{
				Lesson l=(Lesson)item;
				count+=l.getMusicItemCount();
				complete+=l.getMusicItemCompleteCount();
				total+=l.getMusicItemTotal();
			}
		}
		if (count>0)
		{
			t+=" - ";
			t+=Math.round(10*total/complete)/10.0+"% ("+(1000*complete/count)/10+"% complete)";
		}
		return t;
	}

	public boolean getAllowsChildren() {return true;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}
