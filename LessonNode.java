package crescendo.lesson;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.tree.TreeNode;



/**
 * 
 */
public class LessonNode implements BookItem {

	public LessonNode(){
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

	@Override
	public List<PageItem> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

}