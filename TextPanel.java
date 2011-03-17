package crescendo.lesson;

import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * 
 */
public class TextPanel extends JPanel {

	public TextPanel(String text){
		super();
		JLabel label= new JLabel(text);
		this.add(label);
		
	}



}