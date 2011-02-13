package crescendo.lesson;

import java.util.List;
import java.util.LinkedList;
import javax.swing.JPanel;

public class Chapter implements BookItem,BookItemReceiver
{
	private String title;
	private List<BookItem> contents;
	private List<PageItem> items;
	
	public Chapter(String title)
	{
		this.title=title;
		this.contents=new LinkedList<BookItem>();
		this.items=new LinkedList<PageItem>();
	}
	
	public List<BookItem> getContents()
	{
		return this.contents;
	}
	
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	public void loadChildren()
	{
	}
	
	public JPanel getPanel()
	{
		return null;
	}
}
