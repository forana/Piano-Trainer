package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.ErrorHandler;
import crescendo.base.ErrorHandler.Response;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SongSelectionScreen extends JPanel {

	private JLabel Song1 = new JLabel("Song1");
	private JButton LoadFile = new JButton("Load Song File");
	private EventListener l = new EventListener();
	private SheetMusic module;
	public SongSelectionScreen(SheetMusic module,int width, int height){
		this.module = module;
		this.setSize(width, height);

		Song1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Song1.setSize(width, height/10);

		Map songs = null;// = ProfileManager.getInstance().getSongList();

		parseSongList(songs);
		LoadFile.addActionListener(l);

		this.add(LoadFile);


	}
	
	private JPanel getPane(){
		return this;
	}

	private void parseSongList(Map songs) {
		// TODO Parse out the Filename, and the meta information.

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
			module.loadSong(loadedSong,0);
		}
	}


	private class EventListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == LoadFile){
				JFileChooser jfc = new JFileChooser();
				
				int returnVal = jfc.showOpenDialog(SongSelectionScreen.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					loadSong(file.getAbsolutePath());
				}

			}

		}

	}


}
