package crescendo.base.GUI;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;
import crescendo.base.module.Module;

/**
 * ProfileModule
 * 
 * This module is used to modify the attributes of a profile,
 * the preferences of a profile, and manage scores data for that
 * profile.
 * 
 * @author groszc
 *
 */
public class ProfileModule extends Module{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -505754189840407725L;
	//general app pref elements
	JButton renameProfileButton;
	JButton deleteProfileButton;
	
	//sheet music pref elements
	JButton deleteSheetMusicScoresButton;
	
	ProfileModule(int width, int height)
	{
		super();
		
		
		//TODO: preferred midi devide (string)
		//TODO: bool for dynamic grading
		//TODO: bool for pitch grading
		//TODO: bool for timing grading
		
		
		//set the size of this module
		this.setSize(width, height);
		this.setLayout(null);
		
		//set up the different tabs for the profile/prefs manager
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 0, 1024, 768);
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		

		//**********   General Profile Preferences   ***************//
		JPanel generalPanel = new JPanel();
		
		renameProfileButton = new JButton("Rename Profile");
		renameProfileButton.addActionListener(al);
		generalPanel.add(renameProfileButton);
		
		deleteProfileButton = new JButton("Delete Profile");
		deleteProfileButton.addActionListener(al);
		generalPanel.add(deleteProfileButton);
		
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>General</body></html>", null, generalPanel,"General Program Preferences");
		
		//**********   Sheet Music Profile Preferences   ***************//
		JPanel sheetMusicPanel = new JPanel();
		deleteSheetMusicScoresButton = new JButton("Delete sheet music scores");
		deleteSheetMusicScoresButton.addActionListener(al);
		sheetMusicPanel.add(deleteSheetMusicScoresButton);
		sheetMusicPanel.setLayout(new BoxLayout(sheetMusicPanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>Sheet Music</body></html>", null, sheetMusicPanel,"Sheet Music Preferences");
		
		
		//**********   Lesson Profile Preferences   ***************//
		JPanel lessonPanel = new JPanel();
		//renameProfileButton = new JButton("Rename Profile");
		//renameProfileButton.addActionListener(al);
		//lessonPanel(renameProfileButton);
		lessonPanel.setLayout(new BoxLayout(lessonPanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>Lessons</body></html>", null, lessonPanel,"Lesson Preferences");
		
		
		
		//**********   Game Profile Preferences   ***************//
		JPanel gamePanel = new JPanel();
		//renameProfileButton = new JButton("Rename Profile");
		//renameProfileButton.addActionListener(al);
		//gamePanel(renameProfileButton);
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>Game</body></html>", null, gamePanel,"Game Preferences");
		
		//add the tabbed pane to ourself
		add(tabbedPane);
	}
	
	/**
	 * Profile/prefs module action listener
	 */
	private ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			//if they chose to rename their profile
			if(e.getSource().equals(renameProfileButton))
			{
				String name = JOptionPane.showInputDialog("Enter new name for profile:");
				
				if(name!=null)
				{
					ProfileManager.getInstance().renameProfile(ProfileManager.getInstance().getActiveProfile(), name);
				}
			
				PianoTrainerApplication.getInstance().updateProfileMenu();
			}
			
			
			//if they chose to delete their profile
			if(e.getSource().equals(deleteProfileButton))
			{
				if(ProfileManager.getInstance().getProfiles().size()<=0)
				{
					JOptionPane.showMessageDialog(null, "You cannot delete this profile unless another profile exists");
				}
				else
				{
					int response = JOptionPane.showConfirmDialog(null, "Delete this profile?", "Warning", JOptionPane.YES_NO_OPTION);
					if(response == JOptionPane.YES_OPTION)
					{
						Profile toDelete = ProfileManager.getInstance().getActiveProfile();
						ProfileManager.getInstance().switchProfile(ProfileManager.getInstance().getProfiles().get(0));
						ProfileManager.getInstance().removeProfile(toDelete);
					
						PianoTrainerApplication.getInstance().updateProfileMenu();
					}
				}
			}
		
		
			//if they chose to delete the sheet music scores
			if(e.getSource().equals(deleteSheetMusicScoresButton))
			{
				int result = JOptionPane.showConfirmDialog(null,"Delete scores for sheet music?");
				
				if(result==1)
				{	
					//TODO: clear scores once implemented
				}
	
			}
		}
	};

	@Override
	public String saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanUp() {
		
	}
	
}
