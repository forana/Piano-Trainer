package crescendo.lesson;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;

import crescendo.base.ErrorHandler;



/**
 *
 */
public class BookNode implements LessonTreeNode,Comparable<BookNode> {

	private LessonBook book;
	private List<LessonTreeNode> children;
	private LessonData data;
	
	public BookNode(LessonData data){
		this.data=data;
		this.book=null;
	}
	
	public BookNode(LessonBook book){
		this.data=book.getData();
		this.book=book;
	}
	
	public LessonData getData() {
		return this.data;
	}

	public JPanel getPanel(){
		JPanel panel=new JPanel();
		if (this.book==null) {
			try {
				this.book=LessonFactory.createLessonBook(this.data);
			}
			catch (IOException e) {
				ErrorHandler.showNotification("Lesson Load Error","There was an error loading the lesson book.\nError: "+e.getMessage());
				return panel;
			}
		}
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		JLabel titleLabel=new JLabel(data.getTitle());
		titleLabel.setFont(new Font(Font.SERIF,Font.BOLD,24));
		panel.add(titleLabel);
		JLabel authorLabel=new JLabel(book.getAuthor());
		authorLabel.setFont(new Font(Font.SERIF,Font.PLAIN,16));
		panel.add(authorLabel);
		return panel;
	}

	public TreeNode getChildAt(int childIndex) {
		return this.book.getContents().get(childIndex);
	}
	
	public boolean isLeaf() {
		return false;
	}
	
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
	
	public int compareTo(BookNode other) {
		return this.data.getTitle().compareToIgnoreCase(other.data.getTitle());
	}
	
	public String toString() {
		return this.data.getTitle();
	}

	public boolean getAllowsChildren() {return true;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}