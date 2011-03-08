package crescendo.lesson;

import crescendo.base.module.Module;

import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.tree.TreeNode;
import javax.swing.JButton;

public class LessonModule extends Module
{
	private RootNode root;
	
	public LessonModule()
	{
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JPanel leftPanel=new JPanel();
		leftPanel.setPreferredSize(new Dimension(200,10));
		leftPanel.setMaximumSize(new Dimension(200,5000));
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
		JButton button=new JButton("Add Lesson Book");
		button.setPreferredSize(new Dimension(200,24));
		button.setMaximumSize(new Dimension(300,24));
		this.root=new RootNode();
		JTree tree=new JTree(this.root);
		tree.setRootVisible(false);
		JScrollPane leftScroll=new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		leftPanel.add(button);
		leftPanel.add(leftScroll);
		this.add(leftPanel);
		JScrollPane rightScroll=new JScrollPane(new JLabel("main pane"),JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(rightScroll);
	}
	
	public String saveState() {
		return null;
	}
	
	public void cleanUp() {
	}
	
	private class RootNode implements TreeNode
	{

		@Override
		public Enumeration children() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean getAllowsChildren() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public TreeNode getChildAt(int childIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getChildCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getIndex(TreeNode node) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public TreeNode getParent() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isLeaf() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
