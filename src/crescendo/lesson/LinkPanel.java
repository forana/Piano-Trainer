package crescendo.lesson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.ErrorHandler;

/**
 * Panel view of a LinkItem.
 * @author forana
 */
public class LinkPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	// the target url
	private String url;
	
	/**
	 * @param url The target URL
	 * @param text The text of the link
	 */
	public LinkPanel(String url, String text) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.url=url;
		this.setBackground(Color.WHITE);
		JLabel label=new JLabel(text);
		Font font=new Font(Font.SERIF,Font.PLAIN,12);
		label.setForeground(Color.BLUE);
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setFont(font);
		label.addMouseListener(this);
		this.add(label);
	}
	
	/**
	 * Event handler for mouse click
	 */
	public void mouseClicked(MouseEvent e) {
		Desktop desktop = Desktop.getDesktop();
		URI uri;
		try {
			uri = new URI(this.url);
			desktop.browse( uri );
		} catch (Exception ex) {
			System.out.println(url);
			ex.printStackTrace();
			ErrorHandler.showNotification("Error","Error opening link");
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}
