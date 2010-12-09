package crescendo.base.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JPanel;

import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;

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
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

	}
	
	
	
	
}
