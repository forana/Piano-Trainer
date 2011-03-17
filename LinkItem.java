package crescendo.lesson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.ErrorHandler;

public class LinkItem implements PageItem
{
	private String url;
	private String text;
	
	public LinkItem(String url,String text)
	{
		this.url=url;
		this.text=text;
	}
	
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		// TODO 
		JPanel panel;
		
		panel = new LinkPanel(url, text);
		
		
		
		
		
		
		
		
		return panel;
	}
}
