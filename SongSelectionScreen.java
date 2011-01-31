package crescendo.sheetmusic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import crescendo.base.profile.ProfileManager;

public class SongSelectionScreen extends JScrollPane {

	private static JPanel songPane = new JPanel();

	private JLabel Song1 = new JLabel("Song1");
	private JButton LoadFile = new JButton("Load Song File");
	private EventListener l = new EventListener();

	public SongSelectionScreen(int width, int height){
		super(songPane);
		this.setSize(width, height);

		Song1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		Song1.setSize(width, height/10);



		Map songs = null;// = ProfileManager.getInstance().getSongList();

		parseSongList(songs);
		LoadFile.addActionListener(l);

		songPane.add(LoadFile);


	}
	
	private JScrollPane getPane(){
		return this;
	}

	private void parseSongList(Map songs) {
		// TODO Parse out the Filename, and the meta information.

	}


	private class EventListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == LoadFile){
				JFileChooser jfc = new JFileChooser();
				
				int returnVal = jfc.showOpenDialog(songPane);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					//This is where a real application would open the file.


				}

			}

		}

	}


}
