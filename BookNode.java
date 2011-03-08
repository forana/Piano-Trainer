package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.tree.TreeNode;



/**
 *
 */
public class BookNode implements LessonTreeNode {

	private LessonBook book;
	private List<LessonTreeNode> children;
	private LessonData data;

	public BookNode(){
		//TODO
	}


	/**
	 * 
	 * @param data
	 */
	public BookNode(LessonData data){
		//TODO
	}

	/**
	 * 
	 * @param book
	 */
	public BookNode(LessonBook book){
		//TODO
	}

	public JPanel getPanel(){
		//TODO
		return null;
	}

	public void loadChildren(){
		//TODO

	}

	public Enumeration children(){
		//TODO
		return null;
	}

	public boolean getAllowsChildren(){
		//TODO
		return false;
	}

	/**
	 * 
	 * @param index
	 */
	public TreeNode getChildAt(int index){
		//TODO
		return null;
	}

	public int getChildCount(){
		//TODO
		return 0;
	}

	/**
	 * 
	 * @param node
	 */
	public int getIndex(TreeNode node){
		//TODO
		return 0;
	}

	public TreeNode getParent(){
		//TODO
		return null;
	}

	public boolean isLeaf(){
		//TODO
		return false;
	}

}