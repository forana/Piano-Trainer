package crescendo.lesson;

import java.util.List;
import javax.swing.JPanel;

public class Lesson implements BookItem
{
	private String title;
	private List<PageItem> items;
	
	public Lesson(String title)
	{
		this.title=title;
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
