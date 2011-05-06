package crescendo.game;

//import javax.swing.JComponent;
//import crescendo.base.HeuristicsModel;
//import crescendo.base.SongValidator;
import java.util.List;

import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
//import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	private String loadedSongPath;
	
	/** a reference to this module engine **/
	GameEngine gameEngine;
	
	public GameModule() {
		this.showSongSelectionPanel();
	}
	
	public void showGamePanel(SongModel model,List<Track> activeTracks,List<Track> audioTracks) {
		EventDispatcher.getInstance().detachAllMidi();
		this.removeAll();
		gameEngine = new GameEngine(this,model,activeTracks,audioTracks);
		this.add(gameEngine);
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
	
	public void showScorePanel(SongModel model,List<Track> activeTracks,List<Track> audioTracks,ScoreCalculator calc) {
		this.removeAll();
		this.add(new GameResultsPanel(this,model,activeTracks,audioTracks,calc));
		this.updateUI();
	}
	
	@Override
	public String saveState() {
		return loadedSongPath;
	}

	@Override
	public void cleanUp() {
		if(gameEngine!=null)gameEngine.stop();
		EventDispatcher.getInstance().detachAllMidi();
		
	}
}
