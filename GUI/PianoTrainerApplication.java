package crescendo.base.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;


/**
 * PianoTrainerApplication
 * 
 * 	Entry point of application.
 * 
 * @author larkinp, groszc
 *
 */
public class PianoTrainerApplication {
	
	private  JFrame mainWindow;
	private  JMenuBar menuBar;
	
	/** drop down menu of profiles to select **/
	private  JMenu profileMenu;
	
	/** list of profile buttons (stored to find which was clicked) **/
	private  ArrayList<JMenuItem> profiles;
	
	/** an "Add Profile..." shortcut button (below all the profiles) **/
	private  JMenuItem addProfileButton;
	
	/** The ProfileManager of the application **/
	private ProfileManager profileManager;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PianoTrainerApplication();
	}
	
	/**
	 * PianoTrainerApplication
	 * 
	 * Default Constructor
	 */
	PianoTrainerApplication()
	{
		init();
	}
	
	public void init(){
		mainWindow = new JFrame();
		mainWindow.setSize(1024, 768);
		mainWindow.setVisible(true);
		menuBar = new JMenuBar();
		
		//boolean profilesLoaded=false;
		//TODO: try to load ProfileManager from file
		//if(!profilesLoaded)
		//{
			profileManager = ProfileManager.getInstance();
			
					
		
		
		//}
		
		profileMenu = new JMenu(profileManager.getActiveProfile().getName());
		updateProfileMenu();
		
		menuBar.add(profileMenu);
		
		mainWindow.setJMenuBar(menuBar);
		
	}
	
	
	
	/**
	 * Add a module or frame to the window
	 * @param c
	 * @return
	 */
	public static boolean add(JComponent c){
		return false;
	}
	
	
	
	
	/**
	 * Remove a module or frame from the window
	 * @param c
	 * @return
	 */
	public static boolean remove(JComponent c){
		return false;
	}
	
	
	/**
	 * updateProfileMenu
	 * 
	 * private helper function to separate concerns
	 */
	private void updateProfileMenu()
	{
		profileMenu.setText(profileManager.getActiveProfile().getName());
		profileMenu.removeAll();
		
		profiles = new ArrayList<JMenuItem>();
		
		
		for(Profile p: profileManager.getProfiles())
		{
			JMenuItem profileButton = new JMenuItem(p.getName());
			profileButton.addActionListener(al);
			profiles.add(profileButton);
		}
		
		addProfileButton = new JMenuItem("Add Profile...");
		addProfileButton.addActionListener(al);
		
		for(JMenuItem p: profiles)
		{
			profileMenu.add(p);
		}
		profileMenu.add(addProfileButton);
	}


	
	
	/**
	 * Profile menu action listener (for switching profile/ add new profile shortcut)
	 */
	private ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			//if they chose to add a new profile
			if(e.getSource().equals(addProfileButton))
			{
				String name = JOptionPane.showInputDialog("Enter Profile Name:");
				
				Profile newProfile = new Profile(name);
				profileManager.addProfile(newProfile);	
			}
			
			//if they selected a differant profile
			for(JMenuItem p:profiles)
			{
				if(e.getSource().equals(p))
				{
					
					profileManager.switchProfile(profileManager.getProfileByName(p.getText()));	
				}
			}
			
			
			
			
			//
			//reorganize the profile menu
			//
			
			updateProfileMenu();
			
		}
	};
}
