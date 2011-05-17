package crescendo.base.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import javax.swing.JPanel;

import crescendo.base.profile.Profile;


public class MenuFrame extends JPanel{
	
	private Profile activeProfile;
	private ButtonEventListener bEL;
	private JButton GameButton;
	
	private JButton LessonButton;

	private JButton SheetMusicButton;

		
	public MenuFrame(int width, int height){
		super();
		this.setSize(width, height-20);
		bEL = new ButtonEventListener();
		GameButton = new JButton("Game");
		LessonButton = new JButton("Lesson");
		SheetMusicButton = new JButton("Sheet Music");
		this.setLayout(null);
		GameButton.setBounds(300, 200 , width-600, height/8);
		LessonButton.setBounds(300, 200+(height/8)+25, width-600, height/8);
		SheetMusicButton.setBounds(300, 200+2*((height/8)+25), width-600, height/8);
		//TODO Set Size and Position of these buttons
		add(GameButton);
		add(LessonButton);
		add(SheetMusicButton);
	}
	

	/**
	 * @author larkinp
	 * @version 1.0
	 * @created 26-Oct-2010 7:31:47 PM
	 */
	private class ButtonEventListener implements ActionListener {

		
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO: Nothing at this time later implement switching between modules, once we have modules.
			AbstractButton b = (AbstractButton) e.getSource();
			
			//PianoTrainerApplication.switchModules(b.getText());
		}

	}
	
	
	
	
}
