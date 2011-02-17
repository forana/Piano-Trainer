package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import crescendo.base.ErrorHandler;
import crescendo.base.ErrorHandler.Response;
import crescendo.base.profile.ProfileManager;
import crescendo.base.profile.SongPreference;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SongSelectionScreen extends JPanel {

	private JLabel Song1 = new JLabel("Song1");
	private JButton LoadFile = new JButton("Load Song File");
	private EventListener l = new EventListener();
	private SheetMusic module;
	private List<SongPreference> s;
	private JList list;
	private int width, height;

	public SongSelectionScreen(SheetMusic module,int width, int height){
		this.setBackground(Color.WHITE);
		this.module = module;
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		Song1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//Song1.setSize(width, height/10);

		LoadFile.addActionListener(l);

		this.add(LoadFile,BorderLayout.NORTH);
		
		list=new JList();
		list.addListSelectionListener(l);
		list.setCellRenderer(new CustomCellRenderer());
		list.setFixedCellHeight(32);
		this.add(list,BorderLayout.CENTER);

		parseSongList();
	}

	private JPanel getPane(){
		return this;
	}

	private void parseSongList() {
		s = ProfileManager.getInstance().getActiveProfile().getSongPreferences();
		Object[] items=new Object[s.size()];
		for (int i=0; i<items.length; i++)
		{
			items[i]=new ListItem(s.get(i));
		}
		list.setListData(items);
	}

	private void loadSong(String filename){
		File file = new File(filename);
		SongModel loadedSong = null;
		boolean loading = true;
		try {
			while(loading){
				loadedSong = SongFactory.generateSongFromFile(file.getAbsolutePath());
				loading = false;
			}
		} catch (IOException e1) {
			Response response = ErrorHandler.showRetryFail("Failed to load song", "Application failed to load song: "+file.getAbsolutePath()+" would you like to try again?");
			if(response == Response.RETRY){
				loading = true;
			}else{
				loading = false;
			}
		}
		if(loadedSong!=null){
			SongPreference newSong = new SongPreference(filename, loadedSong.getTracks().size(), 0);
			newSong.setSongName(loadedSong.getTitle());
			if(loadedSong.getCreators().size()>0)
				newSong.setCreator(loadedSong.getCreators().get(0).getName());
			else
				newSong.setCreator("");
			boolean doAdd = true;
			for(SongPreference p : ProfileManager.getInstance().getActiveProfile().getSongPreferences()){
				if(p.getFilePath().equals(filename)){
					doAdd=false;
				}
			}
			if(doAdd){
				ProfileManager.getInstance().getActiveProfile().getSongPreferences().add(newSong);
			}

			module.loadSong(loadedSong,0);

		}
	}


	private class EventListener implements ActionListener,ListSelectionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == LoadFile) {
				JFileChooser jfc = new JFileChooser();

				int returnVal = jfc.showOpenDialog(SongSelectionScreen.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					loadSong(file.getAbsolutePath());
				}

			}

		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			Object obj=list.getModel().getElementAt(e.getFirstIndex());
			if(obj instanceof ListItem){
				String songPath = ((ListItem)(obj)).getPath();
				loadSong(songPath);
			}
		}

	}

	private class ListItem
	{
		private String title;
		private String path;
		
		public ListItem(SongPreference pref)
		{
			this.title=pref.getSongName()+(pref.getCreator()==null?"":" - "+pref.getCreator());
			this.path=pref.getFilePath();
		}
		
		public String getPath()
		{
			return this.path;
		}
		
		public String toString()
		{
			return this.title;
		}
	}
	
	private class CustomCellRenderer extends JLabel implements ListCellRenderer
	{
		public Component getListCellRendererComponent(JList list, Object obj, int index, boolean selected, boolean hasFocus) {
			this.setText(obj.toString());
			this.setBorder(new LineBorder(new Color(0x99,0x99,0x99)));
			
			return this;
		}
	}
}
