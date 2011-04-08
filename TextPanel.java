package crescendo.lesson;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public TextPanel(String text) {
		super();
		JLabel label= new JLabel();
		label.setText(text);
		label.setFont(new Font(Font.SERIF,Font.PLAIN,12));
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.add(label);
	}
}