package crescendo.lesson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
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
		if (this.book==null) {
			try {
				this.book=LessonFactory.createLessonBook(this.data);
			}
			catch (IOException e) {
				ErrorHandler.showNotification("Lesson Load Error","There was an error loading the lesson book.\nError: "+e.getMessage());
				return new JPanel();
			}
		}
		
		class LabelPanel extends JPanel {
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
		
		JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBackground(Color.WHITE);
		
		panel.add(new LabelPanel(this.book.getTitle(),true,32));
		if (this.book.getAuthor()!=null) {
			panel.add(new LabelPanel(this.book.getAuthor(),true,18));
		}
		if (this.book.getLicense()!=null) {
			JPanel license=new LabelPanel(this.book.getLicense(),false,16,this.book.getLicenseURL()!=null);
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
		String title=this.data.getTitle();
		
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
			title+=Math.round(1000*total/complete)/10.0+"% ("+(1000*complete/count)/10+"% complete)";
		}
		
		return title;
	}

	public boolean getAllowsChildren() {return true;}
	public int getIndex(TreeNode node) {return 0;}
	public TreeNode getParent() {return null;}
	public Enumeration<TreeNode> children() {return null;}
}