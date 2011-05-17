package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

/**
 * Represents a single lesson in a JTree heirarchy.
 * @author forana
 */
public class Lesson implements BookItem,LessonTreeNode
{
	// title of the lesson
	private String title;
	// page items
	private List<PageItem> items;
	
	/**
	 * @param title The title of the lesson.
	 * @param items Items of which this lesson is comprised.
	 */
	public Lesson(String title,List<PageItem> items)
	{
		this.title=title;
		this.items=items;
	}
	
	/**
	 * @return The title of the lesson.
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
	 * @return A JPanel representing this lesson.
	 */
	public JPanel getPanel(JComponent module)
	{
		return new LessonPanel(this,module);
	}
	
	/**
	 * @return The title of this lesson, with a score appended (if one can be calculated for this lesson).
	 */
	public String toString()
	{
		String t=this.title;
		int count=getMusicItemCount();
		int complete=getMusicItemCompleteCount();
		double total=getMusicItemTotal();
		if (count>0)
		{
			t+=" - ";
			t+=Math.round(10*total/complete)/10.0+"% ("+(1000*complete/count)/10+"% complete)";
		}
		return t;
	}
	
	/**
	 * @return The child node at the specified index.
	 */
	public TreeNode getChildAt(int childIndex) {
		return null;
	}
	
	/**
	 * @return Whether or not this node can have children.
	 */
	public boolean isLeaf() {
		return true;
	}
	
	/**
	 * @return How many children this node has.
	 */
	public int getChildCount() {
		return 0;
	}
	
	/**
	 * @return The number of MusicItems among this lesson's page items.
	 */
	public int getMusicItemCount() {
		int count=0;
		for (PageItem item : this.items) {
			if (item instanceof MusicItem) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * @return The total score (not averaged) for completed music items.
	 */
	public double getMusicItemTotal() {
		double total=0;
		for (PageItem item : this.items) {
			if (item instanceof MusicItem) {
				LessonGrade grade=((MusicItem)item).getLessonData().getGrade(((MusicItem)item).getCode());
				if (grade.isComplete())
				{
					total+=grade.getGrade();
				}
			}
		}
		return total;
	}
	
	/**
	 * @return The number of completed MusicItems among this lesson's page items.
	 */
	public int getMusicItemCompleteCount() {
		int count=0;
		for (PageItem item : this.items) {
			if (item instanceof MusicItem) {
				if (((MusicItem)item).getLessonData().getGrade(((MusicItem)item).getCode()).isComplete())
				{
					count++;
				}
			}
		}
		return count;
	}

	public boolean getAllowsChildren() {return false;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}
