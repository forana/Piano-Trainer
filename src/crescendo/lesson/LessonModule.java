package crescendo.lesson;

import crescendo.base.module.Module;
import crescendo.base.ErrorHandler;
import crescendo.base.profile.ProfileManager;
import crescendo.base.GUI.LeftResizer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JButton;

/**
 * Represents the lesson module itself.
 * @author forana
 */
public class LessonModule extends Module implements ActionListener,MouseListener
{
	private static final long serialVersionUID=1L;
	
	private List<BookNode> books;
	private JButton loadButton;
	private JTree tree;
	private RootNode root;
	private JScrollPane rightScroll;
	
	/**
	 * 
	 */
	public LessonModule()
	{
		this.loadTree();
		
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JPanel leftPanel=new JPanel();
		leftPanel.setPreferredSize(new Dimension(200,10));
		leftPanel.setMaximumSize(new Dimension(200,5000));
		leftPanel.setLayout(new BoxLayout(leftPanel,BoxLayout.Y_AXIS));
		this.loadButton=new JButton("Add Lesson Book");
		this.loadButton.setPreferredSize(new Dimension(200,24));
		this.loadButton.setMaximumSize(new Dimension(300,24));
		this.loadButton.addActionListener(this);
		this.root=new RootNode();
		this.tree=new JTree(this.root);
		this.tree.setRootVisible(true);
		this.tree.addMouseListener(this);
		JScrollPane leftScroll=new JScrollPane(this.tree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		leftPanel.add(this.loadButton);
		leftPanel.add(leftScroll);
		this.add(leftPanel);
		JSeparator sep=new LeftResizer(leftPanel,this);
		sep.setPreferredSize(new Dimension(5,2000));
		sep.setMaximumSize(new Dimension(5,2000));
		sep.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		this.add(sep);
		JPanel base=new JPanel();
		base.setBackground(Color.WHITE);
		JLabel titleLabel=new JLabel("Add a lesson book or select a lesson.");
		base.add(titleLabel);
		this.rightScroll=new JScrollPane(base,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.rightScroll.setBackground(Color.WHITE);
		this.add(this.rightScroll);
		
		this.rightScroll.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e)
			{
				rightScroll.getViewport().getView().setMaximumSize(new Dimension(getWidth(),Integer.MAX_VALUE));
			}
			
			public void componentShown(ComponentEvent e)
			{
				componentResized(e);
			}

			public void componentHidden(ComponentEvent e){}
			public void componentMoved(ComponentEvent e){}
		});
	}
	
	// Load tree entries from file
	private void loadTree() {
		this.books=new ArrayList<BookNode>();
		for (LessonData data : ProfileManager.getInstance().getActiveProfile().getLessonData())
		{
			this.books.add(new BookNode(data));
		}
		Collections.sort(this.books);
		if (this.tree!=null) {
			this.tree.updateUI();
		}
	}
	
	@Override
	public String saveState() {
		return "";
	}
	
	@Override
	public void cleanUp() {
		LessonFactory.clean();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.loadButton)
		{
			JFileChooser fc=new JFileChooser(ProfileManager.getInstance().getActiveProfile().getLastDirectory());
			fc.addChoosableFileFilter(new FileNameExtensionFilter("Lesson Books (*.tlb,*.zip)","tlb","zip"));
			if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
			{
				File f=fc.getSelectedFile();
				ProfileManager.getInstance().getActiveProfile().setLastDirectory(f.getParentFile());
				try
				{
					LessonBook book=LessonFactory.createLessonBook(f.getAbsolutePath());
					ProfileManager.getInstance().getActiveProfile().getLessonData().add(book.getData());
					this.books.add(new BookNode(book));
					Collections.sort(this.books);
					if (this.tree!=null) {
						this.tree.updateUI();
					}
				}
				catch (IOException ex)
				{
					ErrorHandler.showNotification("Error Loading Lesson","The file \""+f.getName()+"\" was in an unexpected format.\nError response: "+ex.getMessage());
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		TreePath path=this.tree.getPathForLocation(e.getX(),e.getY());
		if (path!=null)
		{
			this.tree.setSelectionPath(path);
			Object item=path.getLastPathComponent();
			if (item instanceof LessonTreeNode)
			{
				if (e.getButton()==MouseEvent.BUTTON3 && item instanceof BookNode)
				{
					final LessonData data=((BookNode)item).getData();
					JPopupMenu menu=new JPopupMenu();
					JMenuItem renameItem=new JMenuItem("Rename");
					renameItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String name=JOptionPane.showInputDialog(null,"New name for \""+data.getTitle()+"\":");
							if (name!=null && !name.equals("")) {
								data.setTitle(name);
								loadTree();
							}
						}});
					menu.add(renameItem);
					JMenuItem deleteItem=new JMenuItem("Delete");
					deleteItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							ProfileManager.getInstance().getActiveProfile().getLessonData().remove(data);
							loadTree();
						}});
					menu.add(deleteItem);
					menu.show(this.tree,e.getX(),e.getY());
				}
				else if (e.getButton()==MouseEvent.BUTTON1)
				{
					LessonTreeNode node=(LessonTreeNode)item;
					this.rightScroll.setViewportView(node.getPanel(this.tree));
				}
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Class to represent the root node without erroring.
	 * @author forana
	 */
	private class RootNode implements TreeNode
	{
		public TreeNode getChildAt(int childIndex) {
			return books.get(childIndex);
		}
		
		public boolean isLeaf() {
			return false;
		}
		
		public int getChildCount() {
			return books.size();
		}
		
		public String toString() {
			return "Lesson Books";
		}

		public boolean getAllowsChildren() {return false;}
		public int getIndex(TreeNode node) {return 0;}
		public TreeNode getParent() {return null;}
		public Enumeration<TreeNode> children() {return null;}
	}
}
