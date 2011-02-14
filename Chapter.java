package crescendo.lesson;

import java.util.List;
import java.util.LinkedList;
import javax.swing.JPanel;

public class Chapter implements BookItem
{
	private String title;
	private List<BookItem> contents;
	private List<PageItem> items;
	
	public Chapter(String title,List<BookItem> contents,List<PageItem> items)
	{
		this.title=title;
		this.contents=contents;
		this.items=items;
	}
	
	public List<BookItem> getContents()
	{
		return this.contents;
	}
	
	public List<PageItem> getItems()
	{
		return this.items;
	}
	
	public JPanel getPanel()
	{
		return null;
	}
}
