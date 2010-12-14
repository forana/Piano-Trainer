package crescendo.base.GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
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
	
	/** The Help Menu of the application */
	private JMenu helpMenu;
	
	private JMenuItem helpAbout;
	private JMenuItem helpItem;
	private MenuFrame mainFrame;
	
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
		mainWindow.setTitle("Piano Trainer");
		//boolean profilesLoaded=false;
		//TODO: try to load ProfileManager from file
		//if(!profilesLoaded)
		//{
			profileManager = ProfileManager.getInstance();
			
					
		
		
		//}
		
		profileMenu = new JMenu(profileManager.getActiveProfile().getName());
		updateProfileMenu();
		menuBar.setLayout(new BorderLayout());
		menuBar.add(profileMenu, BorderLayout.WEST);
		
		helpMenu=new JMenu("?");
		
		helpAbout = new JMenuItem("About");
		helpItem = new JMenuItem("Help");
		helpMenu.add(helpAbout);
		helpMenu.add(helpItem);
		helpAbout.addActionListener(al);
		helpItem.addActionListener(al);
		
		menuBar.add(helpMenu,BorderLayout.EAST);
		helpMenu.setAlignmentX((float) 500);
		
		mainWindow.setJMenuBar(menuBar);
		
		mainFrame = new MenuFrame(1024, 768);
		mainWindow.add(mainFrame);
		
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
	 * Handles the switching between two different modules
	 */
	public static void switchModules(Module m){
		
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
			
			if(e.getSource().equals(helpAbout)){
				JOptionPane.showMessageDialog(null, "Infinite Crescendo \n 2010 \n Insert Names");
			}
			//Launching HTML pages in the default browser.
			if(e.getSource().equals(helpItem)){
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				java.net.URI uri;
				try {
					uri = new java.net.URI( "http://johnbokma.com/mexit/2008/08/19/java-open-url-default-browser.html" );
				 
                desktop.browse( uri );
                
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			
			
			//
			//reorganize the profile menu
			//
			
			updateProfileMenu();
			
		}
	};
	
}
