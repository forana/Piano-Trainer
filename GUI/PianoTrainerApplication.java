package crescendo.base.GUI;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



public class PianoTrainerApplication {
	
	private static JFrame mainWindow;
	private static JMenuBar menuBar;
	private static JMenu profileMenu;
	private static JMenuItem activeProfile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		init();
		setActiveProfileText("Hey Look At Me");
	}
	
	public static void init(){
		mainWindow = new JFrame();
		mainWindow.setSize(1024, 768);
		mainWindow.setVisible(true);
		menuBar = new JMenuBar();
		profileMenu = new JMenu("Active Profile");
		activeProfile = new JMenuItem("Corey Look Here");
		
		profileMenu.add(activeProfile);
		
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
	 * Actual method to set the active profile
	 * @param p
	 */
	public static void setActiveProfile(/*Profile p*/){
		
		//TODO This
	}
	/**
	 * Just a testing implementation until profile class is implemented.
	 * @param s
	 */
public static void setActiveProfileText(String s){
	
	profileMenu.setText(s);	
		
	}
}
