package crescendo.game;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import crescendo.base.AudioPlayer;
import crescendo.base.SongPlayer;
import crescendo.base.module.Module;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.SongSelectionScreen;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	JPanel mainAreaTarget = null;
	
	Track activeTrack=null;
	
	SongPlayer songPlayer;
	SongModel songModel;
	AudioPlayer audioPlayer;
	
	GameEngine gameEngine;
	
	public GameModule()
	{
		mainAreaTarget = new JPanel();
		mainAreaTarget.setVisible(true);
		this.setLayout(new BorderLayout());
		
		add(mainAreaTarget,BorderLayout.CENTER);
		
		
		try {
			songModel = SongFactory.generateSongFromFile("C:\\Users\\groszc\\workspace\\crescendo\\Resources\\pkmn-champion.mid");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		songPlayer = new SongPlayer(songModel);
		
		
		showGamePanel(songModel);
	}
	
	public void showGamePanel(SongModel model) {
		mainAreaTarget.removeAll();
		
		gameEngine = new GameEngine(this,model);
		audioPlayer = new AudioPlayer(songModel,activeTrack);
		
		songPlayer.attach(gameEngine, 2000+(int)audioPlayer.getLatency());
		songPlayer.attach(audioPlayer,(int)audioPlayer.getLatency());
		
		
		mainAreaTarget.add(gameEngine);
		
		songPlayer.play();
	}
	
	public void showSongSelectionPanel() {
		//TODO
	}
	
	public void showTrackSelectionPanel(SongModel model) {
		//TODO
	}
	
	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
	}

	@Override
	public String saveState() {
		// TODO Auto-generated method stub
		return null;
	}
}
