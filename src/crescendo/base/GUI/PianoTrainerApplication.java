package crescendo.base.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import crescendo.base.ErrorHandler;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;
import crescendo.game.GameModule;
import crescendo.lesson.LessonModule;
import crescendo.sheetmusic.SheetMusic;
import crescendo.tester.MockMidiDevice;
import crescendo.tester.MockTransmitter;

/**
 * PianoTrainerApplication
 * 
 * Entry point of application.
 * 
 * @author larkinp, groszc
 * 
 */
public class PianoTrainerApplication {

	private static PianoTrainerApplication pianoTrainerApplication = null;

	/** the container of all the other elements on the general app UI **/
	private JFrame mainWindow;

	/** the ui menu **/
	private JMenuBar menuBar;

	/** drop down menu of profiles to select **/
	private JMenu profileMenu;

	/** list of profile buttons (stored to find which was clicked) **/
	private ArrayList<JMenuItem> profiles;

	/** an "Add Profile..." shortcut button (below all the profiles) **/
	private JMenuItem addProfileButton;

	/** The ProfileManager of the application **/
	private ProfileManager profileManager;

	/** The Help Menu of the application */
	private JMenu helpMenu;

	private JMenuItem helpAbout;
	private JMenuItem helpUpdate;
	private JMenuItem helpItem;
	private JPanel moduleFrame;

	/** containers for spacing the menu elements correctly **/
	private Container moduleButtonContainer;

	/** the profile preferences/management button **/
	private JButton preferencesButton;

	/** The three module buttons **/
	private JButton gameButton;
	private JButton lessonButton;
	private JButton sheetMusicButton;

	private SheetMusic sheetMusicModule;
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		try{
		if(args[0].equals("debug")){
			initDebug(args);
		}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("This is a Test");
			//Debugmode is not set
		}
		
		PianoTrainerApplication.getInstance();
	}

	private static void initDebug(String[] args) {
		EventDispatcher.getInstance().setDebug();
		List<Integer> indeces = new ArrayList<Integer>();
		List<String> modifiers = new ArrayList<String>();
		List<Integer> amounts = new ArrayList<Integer>();
		for(int i = 1; i<args.length;i+=3){
			indeces.add(Integer.parseInt(args[i]));
			modifiers.add(args[i+1]);
			amounts.add(Integer.parseInt(args[i+2]));
			
		}
		MockTransmitter.getInstance().setIndexes(indeces);
		MockTransmitter.getInstance().setVariables(modifiers);
		MockTransmitter.getInstance().setAmounts(amounts);
		
	}

	/**
	 * PianoTrainerApplication
	 * 
	 * Default Constructor
	 */
	private PianoTrainerApplication() {
		init();
	}

	/**
	 * getInstance
	 * 
	 * This method returns a singleton instance of PianoTrainerApplication
	 * 
	 * @return the singleton PianoTrainerApplication
	 */
	public static PianoTrainerApplication getInstance() {
		if (pianoTrainerApplication == null)
			pianoTrainerApplication = new PianoTrainerApplication();
		return pianoTrainerApplication;
	}

	/**
	 * init
	 * 
	 * This method sets up the user interface elements, adds event listeners,
	 * initializes member attributes, and puts the general application UI
	 * together.
	 * 
	 */
	public void init() {
		// set the the main window
		mainWindow = new JFrame();
		mainWindow.setSize(1024, 768);
		mainWindow.setVisible(true);
		menuBar = new JMenuBar();
		mainWindow.setTitle("Piano Trainer");

		// load up the icon
		List<Image> icons = new LinkedList<Image>();
		icons.add(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/keys64.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/keys32.png"));
		icons.add(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/keys16.png"));
		mainWindow.setIconImages(icons);

		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.addWindowListener(new WindowListener() {

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosing(WindowEvent arg0) {
				ProfileManager.getInstance().saveToFile("pianoData.PT");
				if (moduleFrame instanceof Module) {
					((Module) moduleFrame).cleanUp();
				}
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowOpened(WindowEvent arg0) {
			}

		});

		// initialize the profile manager
		profileManager = ProfileManager.getInstance();

		menuBar.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// c.fill=GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0;

		// set up the profile selection menu
		profileMenu = new JMenu(profileManager.getActiveProfile().getName());
		profileMenu.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				"resources/icons/arrow.png")));
		updateProfileMenu();

		// add the profile selection menu to the menu bar
		menuBar.add(profileMenu, c);

		// set up the profile/preferences button
		preferencesButton = new JButton("Options");
		preferencesButton.addActionListener(al);
		preferencesButton.setSize(50, -1);

		menuBar.add(preferencesButton, c);

		// set up the container of the module switching buttons
		moduleButtonContainer = new Container();
		moduleButtonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

		// create the module switching buttons
		gameButton = new JButton("Game");
		gameButton.addActionListener(al);
		lessonButton = new JButton("Lesson");
		lessonButton.addActionListener(al);
		sheetMusicButton = new JButton("Sheet Music");

		sheetMusicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sheetMusicModule = new SheetMusic();
				profileManager.getActiveProfile().setLastModule("SheetMusic");
				switchModules(sheetMusicModule);
			}
		});

		lessonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				profileManager.getActiveProfile().setLastModule("Lesson");
				switchModules(new LessonModule());
			}
		});

		gameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				profileManager.getActiveProfile().setLastModule("Game");
				switchModules(new GameModule());
			}
		});

		// add the module buttons to the container
		moduleButtonContainer.add(new JLabel("Mode: "));
		moduleButtonContainer.add(gameButton, BorderLayout.WEST);
		moduleButtonContainer.add(lessonButton, BorderLayout.CENTER);
		moduleButtonContainer.add(sheetMusicButton, BorderLayout.EAST);

		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		menuBar.add(moduleButtonContainer, c);

		// some more spacing work
		Container spacing = new Container();
		spacing.setPreferredSize(new Dimension(300, 0));

		// set up the help/about menu button
		helpMenu = new JMenu("?");

		helpAbout = new JMenuItem("About");
		helpUpdate = new JMenuItem("Update");
		helpItem = new JMenuItem("Help");
		helpMenu.add(helpAbout);
		helpMenu.add(helpUpdate);
		helpMenu.add(helpItem);
		helpAbout.addActionListener(al);
		helpUpdate.addActionListener(al);
		helpItem.addActionListener(al);

		// add the help/about menu button to the menu bar
		c.weightx = 0;
		c.anchor = GridBagConstraints.EAST;
		menuBar.add(helpMenu, c);

		// set the menu bar of the main frame
		mainWindow.setJMenuBar(menuBar);

		// "default" module for startup
		moduleFrame = new JPanel();
		moduleFrame.setBackground(Color.WHITE);
		moduleFrame.add(new JLabel("Select a mode above to get started."));

		// add the module to the main window
		mainWindow.add(moduleFrame);

		// check to see if there are any profiles/preferences to load, if not
		// ask the user for a profile name
		boolean profilesLoaded = profileManager.loadFromFile("pianoData.PT");
		if (!profilesLoaded) {
			String profileName = JOptionPane
					.showInputDialog("Welcome to PianoTrainer! Please enter a name for your profile.");

			if (profileName != null)
				profileManager.getActiveProfile().setName(profileName);

		}

		updateProfileMenu();

	}

	/**
	 * updateProfileMenu
	 * 
	 * helper function to update the contents of the profile menu in case
	 * someone (like the profilemanager) modifies the information displayed
	 * there.
	 */
	public void updateProfileMenu() {
		// basically we remove everything from the profilemenu and
		// put everything back in fresh from the profilemanager

		profileMenu.setText(profileManager.getActiveProfile().getName());
		profileMenu.removeAll();

		profiles = new ArrayList<JMenuItem>();

		for (Profile p : profileManager.getProfiles()) {
			JMenuItem profileButton = new JMenuItem(p.getName());
			profileButton.addActionListener(al);
			profiles.add(profileButton);
		}

		addProfileButton = new JMenuItem("Add Profile...");
		addProfileButton.addActionListener(al);

		for (JMenuItem p : profiles) {
			profileMenu.add(p);
		}
		profileMenu.add(addProfileButton);

	}

	/**
	 * Handles the switching between different modules
	 */
	public void switchModules(Module m) {
		// TODO: handle set-up and tear-down for modules
		mainWindow.remove(moduleFrame);
		if (moduleFrame instanceof Module) {
			((Module) moduleFrame).cleanUp();
		}
		moduleFrame = m;
		mainWindow.add(moduleFrame);
		moduleFrame.updateUI();
	}

	/**
	 * Menu action listener (for switching profile/ add new profile, module
	 * switching, and help/about/update
	 */
	private ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			// if they chose to go to profile preferences
			if (e.getSource().equals(preferencesButton)) {
				switchModules(new ProfileModule());
			}

			// if they chose to add a new profile
			if (e.getSource().equals(addProfileButton)) {

				String name = "";
				while (name != null && name.length() < 1) {
					name = JOptionPane.showInputDialog(addProfileButton,
							"Enter Profile Name:", "New Profile", 1);
					if (name != null && name.length() < 1)
						JOptionPane.showMessageDialog(null, "Invalid name");
				}

				if (name != null) {
					Profile newProfile = new Profile(name);
					if (profileManager.addProfile(newProfile)) {
						profileManager.switchProfile(newProfile);
					}
				}
			}

			// if they selected a different profile
			for (JMenuItem p : profiles) {
				if (e.getSource().equals(p)) {
					profileManager.switchProfile(profileManager
							.getProfileByName(p.getText()));

					if (profileManager.getActiveProfile().getLastModule()
							.equals("Game")) {
						switchModules(new GameModule());
					} else if (profileManager.getActiveProfile()
							.getLastModule().equals("Lesson")) {
						switchModules(new LessonModule());
					} else if (profileManager.getActiveProfile()
							.getLastModule().equals("SheetMusic")) {
						switchModules(new SheetMusic());
					} else {
						mainWindow.remove(moduleFrame);

						// "default" module for startup
						moduleFrame = new JPanel();
						moduleFrame.setBackground(Color.WHITE);
						moduleFrame.add(new JLabel(
								"Select a mode above to get started."));

						mainWindow.add(moduleFrame);
					}

				}
			}

			if (e.getSource().equals(helpAbout)) {
				JOptionPane
						.showMessageDialog(
								mainWindow,
								"Piano Trainer\nmade by Team Infinite Crescendo\n2010-2011\n\nAlex Foran\nNick Gartmann\nCorey Grosz\nPatrick Larkin\n\n<html><font color=\"#0000CF\"><u>http://www.infinitecrescendo.com/</u></font></html>",
								"About Piano Trainer", 1);
			}

			if (e.getSource().equals(helpUpdate)) {
				// TODO: run the updater
				JOptionPane.showMessageDialog(mainWindow,
						"Action currently unsupported", "Update Piano Trainer",
						1);
			}

			// Launching HTML pages in the default browser.
			if (e.getSource().equals(helpItem)) {
				if (!Desktop.isDesktopSupported()) {
					System.err.println("Desktop is not supported (fatal)");
					System.exit(1);
				}

				Desktop desktop = Desktop.getDesktop();

				if (!desktop.isSupported(Desktop.Action.BROWSE)) {
					System.err
							.println("Desktop doesn't support the browse action (fatal)");
					System.exit(1);
				}

				URI uri;
				try {
					uri = new URI("http://infinitecrescendo.com/manual.pdf"); 
					desktop.browse(uri);
				} catch (Exception ex) {
					ErrorHandler
							.showNotification("Error", "Error opening link");
				}
			}

			// reorganize the profile menu
			updateProfileMenu();
		}
	};

}
