package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.tree.TreeNode;



/**
 * 
 */
public class ChapterNode implements BookItem
{
	private LessonBook book;
	private LessonData data;

	public ChapterNode(){
		//TODO
	}

	public List<PageItem> getPageItems(){
		//TODO
		return null;
	}

	public JPanel getPanel(){
		//TODO
		return null;
	}
	
	public List<PageItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	public TreeNode getChildAt(int childIndex) {
		return null;
	}
	
	public boolean isLeaf() {
		return false;
	}
	
	public int getChildCount() {
		return 0;
	}

	public boolean getAllowsChildren() {return false;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}