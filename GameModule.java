package crescendo.game;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import crescendo.base.AudioPlayer;
import crescendo.base.SongPlayer;
import crescendo.base.module.Module;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	JPanel mainAreaTarget = null;
	
	SongPlayer songPlayer;
	AudioPlayer audioPlayer;
	
	GameEngine gameEngine;
	
	public GameModule()
	{
		this.setLayout(new BorderLayout());
		this.showSongSelectionPanel();
	}
	
	public void showGamePanel(SongModel model,Track activeTrack) {
		this.removeAll();
		this.add(new GameEngine(model,activeTrack));
		this.updateUI();

	}
	
	public void showSongSelectionPanel() {
		this.removeAll();
		this.add(new SongSelectionPanel(this));
		this.updateUI();
	}
	
	public void showTrackSelectionPanel(SongModel model) {
		this.removeAll();
		this.add(new TrackSelectionPanel(this,model));
		this.updateUI();
	}
	
	@Override
	public void cleanUp() {
		if (songPlayer!=null)
		{
			songPlayer.stop();
		}
	}

	@Override
	public String saveState() {
		// TODO Auto-generated method stub
		return null;
	}
}
