package crescendo.lesson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

import crescendo.base.ErrorHandler;

/**
 * A JTree node that represents a lesson book to the user.
 */
public class BookNode implements LessonTreeNode,Comparable<BookNode> {
	
	// the book itself
	private LessonBook book;
	// the data via which the book can be loaded
	private LessonData data;
	
	/**
	 * @param data A LessonData instance from which the can be loaded.
	 */
	public BookNode(LessonData data){
		this.data=data;
		this.book=null;
	}
	
	/**
	 * @param book The book itself.
	 */
	public BookNode(LessonBook book){
		this.data=book.getData();
		this.book=book;
	}
	
	/**
	 * @return The data instance from which this book can be loaded.
	 */
	public LessonData getData() {
		return this.data;
	}
	
	/**
	 * @param module The calling module.
	 * @return An instance of a JPanel to show for this node.
	 */
	public JPanel getPanel(JComponent module){
		// if the book hasn't been loaded yet, load it
		if (this.book==null) {
			try {
				this.book=LessonFactory.createLessonBook(this.data);
			}
			catch (IOException e) {
				ErrorHandler.showNotification("Lesson Load Error","There was an error loading the lesson book.\nError: "+e.getMessage());
				return new JPanel();
			}
		}
		
		// inner class to ease label making
		class LabelPanel extends JPanel {
			private static final long serialVersionUID=1L;
			
			public LabelPanel(String text,boolean bold,int size) {
				this(text,bold,size,false);
			}
			public LabelPanel(String text,boolean bold,int size,boolean link) {
				JLabel label=new JLabel(text);
				Font font=new Font(Font.SERIF,bold?Font.BOLD:Font.PLAIN,size);
				if (link) {
					label.setForeground(Color.BLUE);
					label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
				label.setFont(font);
				this.add(label);
				this.setBackground(Color.WHITE);
			}
		}
		
		// create the panel to be returned
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		
		// title
		panel.add(new LabelPanel(this.book.getTitle(),true,32));
		// author
		if (this.book.getAuthor()!=null) {
			panel.add(new LabelPanel(this.book.getAuthor(),true,18));
		}
		// license
		if (this.book.getLicense()!=null) {
			JPanel license=new LabelPanel(this.book.getLicense(),false,16,this.book.getLicenseURL()!=null);
			// linkify the license
			if (this.book.getLicenseURL()!=null) {
				license.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (e.getButton()==MouseEvent.BUTTON1) {
								Desktop desktop = Desktop.getDesktop();
								URI uri;
								try {
									uri = new URI(book.getLicenseURL());
									desktop.browse( uri );
								} catch (Exception ex) {
									ErrorHandler.showNotification("Error","Error opening link");
								}
							}
						}
					});
			}
			panel.add(license);
		}
		// website
		if (this.book.getWebsite()!=null) {
			JPanel website=new LabelPanel(this.book.getWebsite(),false,16,true);
			website.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getButton()==MouseEvent.BUTTON1) {
							Desktop desktop = Desktop.getDesktop();
							URI uri;
							try {
								uri = new URI(book.getWebsite());
								desktop.browse( uri );
							} catch (Exception ex) {
								ErrorHandler.showNotification("Error","Error opening link");
							}
						}
					}
				});
			panel.add(website);
		}
		
		return panel;
	}

	/**
	 * @return The child node at the specified index.
	 */
	public TreeNode getChildAt(int childIndex) {
		return this.book.getContents().get(childIndex);
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
		if (this.book==null) {
			try {
				this.book=LessonFactory.createLessonBook(this.data);
			}
			catch (IOException e) {
				ErrorHandler.showNotification("Lesson Load Error","There was an error loading the lesson book.\nError: "+e.getMessage());
				return 0;
			}
		}
		return this.book.getContents().size();
	}
	
	@Override
	public int compareTo(BookNode other) {
		return this.data.getTitle().compareToIgnoreCase(other.data.getTitle());
	}
	
	/**
	 * @return The title of this node with the score added.
	 */
	public String toString() {
		String title=this.data.getTitle();
		
		// if there's a score entry for this book, append it
		if (this.data.getGradeMap().keySet().size()>0)
		{
			title+=" - ";
			double total=0;
			int count=0;
			int complete=0;
			for (Integer code : this.data.getGradeMap().keySet())
			{
				count++;
				LessonGrade grade=this.data.getGrade(code);
				if (grade.isComplete())
				{
					complete++;
					total+=grade.getGrade();
				}
			}
			title+=this.data.getScale().getGrade(total/complete).label;
			title+=" ("+Math.round(10*total/complete)/10.0+"%) ("+(1000*complete/count)/10+"% complete)";
		}
		
		return title;
	}

	public boolean getAllowsChildren() {return true;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}