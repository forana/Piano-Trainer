package crescendo.game;

import java.awt.BorderLayout;

import crescendo.base.module.Module;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	public GameModule()
	{
		this.setLayout(new BorderLayout());
		this.add(new SongSelectionPanel(this));
	}
	
	public void showGamePanel(SongModel model,Track activeTrack) {
		//TODO
	}
	
	public void showSongSelectionPanel() {
		//TODO
	}
	
	public void showTrackSelectionPanel(SongModel model) {
		this.removeAll();
		this.add(new TrackSelectionPanel(this,model));
		this.updateUI();
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
