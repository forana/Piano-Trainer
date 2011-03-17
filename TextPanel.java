package crescendo.lesson;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public TextPanel(String text) {
		super();
		JLabel label= new JLabel(text);
		label.setFont(new Font(Font.SERIF,Font.PLAIN,12));
		this.setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(label);
	}
}