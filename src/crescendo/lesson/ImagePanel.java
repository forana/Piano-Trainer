package crescendo.lesson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Panel view of an ImageItem.
 * @author forana
 */
public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param item The ImageItem from which to pull data.
	 */
	public ImagePanel(ImageItem item) {
		this.setBackground(Color.WHITE);
		Font font=new Font(Font.SERIF,Font.PLAIN,12);
		JPanel panel=new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		ImageIcon icon=new ImageIcon(item.getSource());
		JLabel label;
		if (icon.getImageLoadStatus()==MediaTracker.ERRORED) {
			label=new JLabel(item.getAlt());
			label.setBorder(new LineBorder(Color.LIGHT_GRAY,1));
		} else {
			label=new JLabel(icon);
			label.setToolTipText(item.getAlt());
		}
		label.setFont(font);
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.add(label,BorderLayout.CENTER);
		if (item.getFooter()!=null) {
			label=new JLabel(item.getFooter());
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setFont(font);
			panel.add(label,BorderLayout.SOUTH);
		}
		this.add(panel);
	}
}