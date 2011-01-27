package crescendo.base.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;
import crescendo.sheetmusic.SheetMusic;


/**
 * PianoTrainerApplication
 * 
 * 	Entry point of application.
 * 
 * @author larkinp, groszc
 *
 */
public class PianoTrainerApplication {
	
	private static PianoTrainerApplication pianoTrainerApplication=null;
	
	/** the container of all the other elements on the general app UI **/
	private  JFrame mainWindow;
	
	/** the ui menu **/
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
	private JPanel moduleFrame;
	
	/** containers for spacing the menu elements correctly **/
	private Container buttonContainer;
	private Container moduleButtonContainer;
	private Container spacingContainer;
	
	/** the profile preferences/management button **/
	private JButton preferencesButton;
	
	/** The three module buttons **/
	private JButton gameButton;
	private JButton lessonButton;
	private JButton sheetMusicButton;
	
	/** the profileModule to display when asked to **/
	private ProfileModule profileModule;
	
	private SheetMusic sheetMusicModule;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PianoTrainerApplication.getInstance();
	}
	
	/**
	 * PianoTrainerApplication
	 * 
	 * Default Constructor
	 */
	private PianoTrainerApplication()
	{
		init();
	}
	
	/**
	 * getInstance
	 * 
	 * This method returns a singleton instance of PianoTrainerApplication
	 * @return the singleton PianoTrainerApplication
	 */
	public static PianoTrainerApplication getInstance()
	{
		if(pianoTrainerApplication==null)pianoTrainerApplication = new PianoTrainerApplication();
		return pianoTrainerApplication;
	}
	
	/**
	 * init
	 * 
	 * This method sets up the user interface elements, adds event listeners, initializes
	 * member attributes, and puts the general application UI together.
	 * 
	 */
	public void init(){
		//set the the main window
		mainWindow = new JFrame();
		mainWindow.setSize(1024, 768);
		mainWindow.setVisible(true);
		menuBar = new JMenuBar();
		mainWindow.setTitle("Piano Trainer");

		//initialize the profile manager
		profileManager = ProfileManager.getInstance();
		
		//initialize the profileModule for use
		profileModule = new ProfileModule(1024, 768);

		
		//create the menu bar on the top of the screen
		menuBar.setLayout(new BorderLayout());
		
		
		//set up the profile selection menu
		profileMenu = new JMenu(profileManager.getActiveProfile().getName());
		updateProfileMenu();
		
		
		//add the profile selection menu to the menu bar
		menuBar.add(profileMenu, BorderLayout.BEFORE_LINE_BEGINS);
		
		//set up the spacing of the topbar
		buttonContainer = new Container();
		buttonContainer.setLayout(new BorderLayout());
		
		//set up the profile/preferences button
		preferencesButton = new JButton("***");
		preferencesButton.addActionListener(al);
		preferencesButton.setSize(50, -1);
		
		buttonContainer.add(preferencesButton, BorderLayout.WEST);
		
		//set up the container of the module switching buttons
		moduleButtonContainer = new Container();
		moduleButtonContainer.setLayout(new BorderLayout());
		
		//create the module switching buttons
		gameButton = new JButton("Game");
		gameButton.addActionListener(al);
		lessonButton = new JButton("Lesson");
		lessonButton.addActionListener(al);
		sheetMusicButton = new JButton("Sheet Music");
		sheetMusicButton.addActionListener(al);
		
		//add the module buttons to the container
		moduleButtonContainer.add(gameButton,BorderLayout.WEST);
		moduleButtonContainer.add(lessonButton,BorderLayout.CENTER);
		moduleButtonContainer.add(sheetMusicButton,BorderLayout.EAST);
		
		// add the module button container to the spacing container
		buttonContainer.add(moduleButtonContainer,BorderLayout.EAST);
		
		//some more spacing work
		Container spacing = new Container();
		spacing.setPreferredSize(new Dimension(300,0));
		
		
		spacingContainer = new Container();
		spacingContainer.setLayout(new BoxLayout(spacingContainer,BoxLayout.X_AXIS));
		
		spacingContainer.add(buttonContainer);
		spacingContainer.add(spacing);
		
		//add the spacing+modulebuttons+spacing container to the menubar
		menuBar.add(spacingContainer, BorderLayout.CENTER);
		
		
		//set up the help/about menu button
		helpMenu=new JMenu("?");
		
		helpAbout = new JMenuItem("About");
		helpItem = new JMenuItem("Help");
		helpMenu.add(helpAbout);
		helpMenu.add(helpItem);
		helpAbout.addActionListener(al);
		helpItem.addActionListener(al);
		
		//add the help/about menu button to the menu bar
		menuBar.add(helpMenu,BorderLayout.EAST);
		helpMenu.setAlignmentX((float) 500);
		
		//set the menu bar of the main frame
		mainWindow.setJMenuBar(menuBar);
		
		
		
		
		
		//for now create a button on a "default" module for startup
		moduleFrame = new JPanel();
		moduleFrame.add(new JButton("ASDF"));
		
		//add the module to the main window
		mainWindow.add(moduleFrame);
		
		
		//check to see if there are any profiles/preferences to load, if not ask the user for a profile name
		boolean profilesLoaded=profileManager.loadFromFile("pianoData.PT");
		if(!profilesLoaded)
		{
			String profileName = JOptionPane.showInputDialog("Welcome to PianoTrainer! Please enter a name for your profile.");
			
			if(profileName!=null)profileManager.getActiveProfile().setName(profileName);
			updateProfileMenu();
		}
		
		
	}
	
	
	/**
	 * updateProfileMenu
	 * 
	 * helper function to update the contents of the profile menu in
	 * case someone (like the profilemanager) modifies the information
	 * displayed there.
	 */
	public void updateProfileMenu()
	{
		//basically we remove everything from the profilemenu and
		//	put everything back in fresh from the profilemanager
		
		
		
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
	 * Handles the switching between different modules
	 */
	public void switchModules(JPanel c){
		//TODO: handle set-up and tear-down for modules
		mainWindow.remove(moduleFrame);
		moduleFrame = c;
		mainWindow.add(moduleFrame);
		moduleFrame.repaint();
	}
	
	
	/**
	 * Menu action listener (for switching profile/ add new profile, module switching, and help/about/update
	 */
	private ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			//if they chose to go to profile preferences
			if(e.getSource().equals(preferencesButton))
			{
				switchModules(profileModule);
			}
			
			
			//if they chose to go to sheet music module 
			if(e.getSource().equals(sheetMusicButton))
			{
				sheetMusicModule = new SheetMusic();
				sheetMusicModule.loadSong("Resources/drawtest2.mid");
				switchModules(sheetMusicModule);
			}
			
			
			
			//if they chose to add a new profile
			if(e.getSource().equals(addProfileButton))
			{
				String name = JOptionPane.showInputDialog("Enter Profile Name:");
				
				if(name!=null)
				{
					Profile newProfile = new Profile(name);
					if(profileManager.addProfile(newProfile))
					{
						profileManager.switchProfile(newProfile);	
					}
				}
			}
			
			//if they selected a different profile
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
			//TODO change the system.out.JOptionPanes.
			if(e.getSource().equals(helpItem)){
				if( !java.awt.Desktop.isDesktopSupported() ) {

		            System.err.println( "Desktop is not supported (fatal)" );
		            System.exit( 1 );
		        }

		        

		        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		        if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

		            System.err.println( "Desktop doesn't support the browse action (fatal)" );
		            System.exit( 1 );
		        }

		        
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
