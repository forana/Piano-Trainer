package crescendo.lesson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;

import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.ErrorHandler;

public class LinkPanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	private String url;
	
	public LinkPanel(String url, String text) {
		this.url=url;
		this.setBackground(Color.WHITE);
		JLabel label=new JLabel(text);
		Font font=new Font(Font.SERIF,Font.PLAIN,12);
		label.setForeground(Color.BLUE);
		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label.setFont(font);
		this.add(label);
		label.addMouseListener(this);
		this.add(label);
	}
	
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
