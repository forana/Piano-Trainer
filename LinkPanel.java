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

public class LinkPanel extends JPanel {
	
	
	
	
	

	public LinkPanel(final String url, String text) {
		
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
		
		
		JPanel website=new LabelPanel(text,false,16,true);
		website.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton()==MouseEvent.BUTTON1) {
						Desktop desktop = Desktop.getDesktop();
						URI uri;
						try {
							uri = new URI(url);
							desktop.browse( uri );
						} catch (Exception ex) {
							ErrorHandler.showNotification("Error","Error opening link");
						}
					}
				}
			});
		this.add(website);

	}
	
	


}
