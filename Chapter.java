package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

/**
 * Represents a single chapter in a JTree heirarchy.
 * @author forana
 */
public class Chapter implements BookItem,LessonTreeNode
{
	// title of the chapter
	private String title;
	// inner lessons / chapters
	private List<BookItem> contents;
	// items for the chapter page itself
	private List<PageItem> items;
	
	/**
	 * @param title Title of the chapter
	 * @param contents Inner lessons / chapters
	 * @param items Items for the chapter page itself
	 */
	public Chapter(String title,List<BookItem> contents,List<PageItem> items)
	{
		this.title=title;
		this.contents=contents;
		this.items=items;
	}
	
	/**
	 * @return All inner lessons / chapters belonging inside this chapter.
	 */
	public List<BookItem> getContents()
	{
		return this.contents;
	}
	
	/**
	 * @return The title of this chapter.
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * @return The items on this lesson.
	 */
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	/**
	 * @param module The calling module.
	 * @return A JPanel representing this chapter.
	 */
	public JPanel getPanel(JComponent module)
	{
		return new ChapterPanel(this,module);
	}
	
	/**
	 * @return The child node at the specified index.
	 */
	public TreeNode getChildAt(int childIndex) {
		return this.contents.get(childIndex);
	}
	
	/**
	 * @return Whether or not this node can have children.
	 */
	public boolean isLeaf() {
		return false;
	}
	
	/**
	 * @return How many children this node has.
	 */
	public int getChildCount() {
		return this.contents.size();
	}
	
	/**
	 * @return The title of this chapter, with a score appended (if one can be calculated for this chapter).
	 */
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
