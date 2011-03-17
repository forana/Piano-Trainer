package crescendo.lesson;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * 
 */
public class ImagePanel extends JPanel {

	private Image image;

	public ImagePanel(String source){
		
		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(new ImageIcon(source));
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.add(label);  // default center section
	}
}