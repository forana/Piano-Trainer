package crescendo.base.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.MidiDevice;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Group;

import crescendo.base.ErrorHandler;
import crescendo.base.profile.Profile;
import crescendo.base.profile.ProfileManager;
import crescendo.base.EventDispatcher.EventDispatcher;
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
public class ProfileModule extends Module implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -505754189840407725L;
	
	//general app pref elements
	private JTextField rename;
	private JButton renameProfileButton;
	private JButton deleteProfileButton;
	private JComboBox deviceList;
	private JButton deviceReload;
	private JButton deviceDetect;
	
	//sheet music pref elements
	private JButton deleteSheetMusicScoresButton;
	
	ProfileModule()
	{
		super();
		
		//set the size of this module
		//this.setSize(width, height);
		this.setLayout(null);
		
		//set up the different tabs for the profile/prefs manager
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 0, 1024, 768);
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);

		//**********   General Profile Preferences   ***************//
		JPanel generalPanel = new JPanel();
		
		GroupLayout layout=new GroupLayout(generalPanel);
		generalPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		JLabel renameLabel=new JLabel("Rename Profile");
		renameLabel.setVerticalAlignment(SwingConstants.CENTER);
		rename=new JTextField(ProfileManager.getInstance().getActiveProfile().getName(),16);
		rename.addActionListener(this);
		renameProfileButton=new JButton("Rename");
		renameProfileButton.addActionListener(this);
		
		deleteProfileButton = new JButton("Delete Profile");
		deleteProfileButton.addActionListener(this);
		
		JLabel deviceLabel=new JLabel("MIDI device:");
		deviceList=new JComboBox();
		this.populateDeviceList();
		deviceList.addActionListener(this);
		deviceReload=new JButton("Reload devices");
		deviceReload.addActionListener(this);
		deviceDetect=new JButton("Autodetect midi device");
		deviceDetect.addActionListener(this);
		
		Group rows=layout.createSequentialGroup();
		rows.addGroup(layout.createParallelGroup().addComponent(renameLabel).addComponent(rename,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addComponent(renameProfileButton));
		rows.addGroup(layout.createParallelGroup().addComponent(deleteProfileButton));
		rows.addGroup(layout.createParallelGroup().addComponent(deviceLabel).addComponent(deviceList,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addComponent(deviceReload).addComponent(deviceDetect));
		Group cols=layout.createSequentialGroup();
		cols.addGroup(layout.createParallelGroup().addComponent(renameLabel).addComponent(deviceLabel));
		cols.addGroup(layout.createParallelGroup().addComponent(rename,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE).addComponent(deleteProfileButton).addComponent(deviceList,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE));
		cols.addGroup(layout.createParallelGroup().addComponent(renameProfileButton).addComponent(deviceReload));
		cols.addGroup(layout.createParallelGroup().addComponent(deviceDetect));
		layout.setHorizontalGroup(cols);
		layout.setVerticalGroup(rows);
		
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>General</body></html>", null, generalPanel,"General Program Preferences");
		
		//**********   Sheet Music Profile Preferences   ***************//
		JPanel sheetMusicPanel = new JPanel();
		deleteSheetMusicScoresButton = new JButton("Delete sheet music scores");
		deleteSheetMusicScoresButton.addActionListener(this);
		sheetMusicPanel.add(deleteSheetMusicScoresButton);
		sheetMusicPanel.setLayout(new BoxLayout(sheetMusicPanel, BoxLayout.Y_AXIS));
		tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>Sheet Music</body></html>", null, sheetMusicPanel,"Sheet Music Preferences");
		
		
		//**********   Lesson Profile Preferences   ***************//
		//JPanel lessonPanel = new JPanel();
		//renameProfileButton = new JButton("Rename Profile");
		//renameProfileButton.addActionListener(al);
		//lessonPanel(renameProfileButton);
		//lessonPanel.setLayout(new BoxLayout(lessonPanel, BoxLayout.Y_AXIS));
		//tabbedPane.addTab("<html><body leftmargin=20 topmargin=8 marginwidth=20 marginheight=5>Lessons</body></html>", null, lessonPanel,"Lesson Preferences");
		
		
		
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
	
	private void populateDeviceList()
	{
		MidiDeviceItem[] items=new MidiDeviceItem[EventDispatcher.getInstance().getTransmitterDevices().size()];
		int selectedIndex=0;
		for (int i=0; i<items.length; i++)
		{
			items[i]=new MidiDeviceItem(EventDispatcher.getInstance().getTransmitterDevices().get(i));
			if (items[i].toString().equals(ProfileManager.getInstance().getActiveProfile().getMidiDeviceName()))
			{
				selectedIndex=i;
			}
		}
		this.deviceList.setModel(new DefaultComboBoxModel(items));
		this.deviceList.setSelectedIndex(selectedIndex);
		this.deviceList.updateUI();
	}
	
	/**
	 * Profile/prefs module action listener
	 */
	public void actionPerformed(ActionEvent e) {
		//if they chose to rename their profile
		if(e.getSource()==renameProfileButton || e.getSource()==rename)
		{
			String name = rename.getText();
			
			if(name!=null)
			{
				ProfileManager.getInstance().renameProfile(ProfileManager.getInstance().getActiveProfile(), name);
			}
		
			PianoTrainerApplication.getInstance().updateProfileMenu();
		}
		//if they chose to delete their profile
		else if(e.getSource().equals(deleteProfileButton))
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
		else if (e.getSource()==deviceList)
		{
			MidiDevice device=((MidiDeviceItem)(deviceList.getItemAt(deviceList.getSelectedIndex()))).getDevice();
			EventDispatcher.getInstance().setTransmitterDevice(device);
			ProfileManager.getInstance().getActiveProfile().updateMidiDevice();
		}
		else if (e.getSource()==deviceReload)
		{
			EventDispatcher.getInstance().loadTransmitterDevices();
			this.populateDeviceList();
		}
		else if (e.getSource()==deviceDetect)
		{
			MidiDevice od=EventDispatcher.getInstance().getCurrentTransmitterDevice();
			ErrorHandler.showNotification("Auto Detect","After pressing OK, press any key(s) on your keyboard.\nYou will have 5 seconds.");
			EventDispatcher.getInstance().loadTransmitterDevices();
			EventDispatcher.getInstance().detectMidiDevice(5);
			MidiDevice nd=EventDispatcher.getInstance().getCurrentTransmitterDevice();
			if (nd==null)
			{
				EventDispatcher.getInstance().setTransmitterDevice(od);
				ErrorHandler.showNotification("Auto Detect","No key presses detected.");
			}
			else
			{
				ErrorHandler.showNotification("Auto Detect","MIDI device set to \""+nd.getDeviceInfo().getName()+"\".");
				ProfileManager.getInstance().getActiveProfile().updateMidiDevice();
				this.populateDeviceList();
			}
		}
	
		//if they chose to delete the sheet music scores
		else if(e.getSource()==deleteSheetMusicScoresButton)
		{
			if(ErrorHandler.showYesNo("Delete Scores","Really delete scores for sheet music?\nThis cannot be undone.")==ErrorHandler.Response.YES)
			{	
				ProfileManager.getInstance().getActiveProfile().getSongPreferences().clear();
			}

		}
	}

	@Override
	public String saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanUp() {
		
	}
	
	private class MidiDeviceItem
	{
		private MidiDevice device;
		
		public MidiDeviceItem(MidiDevice device)
		{
			this.device=device;
		}
		
		public MidiDevice getDevice()
		{
			return this.device;
		}
		
		public String toString()
		{
			return this.device.getDeviceInfo().getName();
		}
	}
}
