package crescendo.lesson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel view of a TextItem.
 * @author forana
 */
public class TextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param text The text to be shown.
	 */
	public TextPanel(String text) {
		super();
		JLabel label= new JLabel();
		label.setText(text);
		label.setFont(new Font(Font.SERIF,Font.PLAIN,12));
		this.setBackground(Color.WHITE);
		// border layout forces it to fill
		this.setLayout(new BorderLayout());
		this.add(label);
	}
}