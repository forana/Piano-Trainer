package crescendo.game;

//import javax.swing.JComponent;
//import crescendo.base.HeuristicsModel;
//import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
//import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	private String loadedSongPath;
	
	public GameModule() {
		this.showSongSelectionPanel();
	}
	
	public void showGamePanel(SongModel model,Track activeTrack) {
		EventDispatcher.getInstance().detachAllMidi();
		this.removeAll();
		this.add(new GameEngine(this,model,activeTrack));
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
	
	public void showScorePanel(SongModel model,Track activeTrack,ScoreCalculator calc) {
		this.removeAll();
		this.add(new GameResultsPanel(this,model,activeTrack,calc));
		this.updateUI();
	}
	
	@Override
	public String saveState() {
		return loadedSongPath;
	}

	@Override
	public void cleanUp() {
		EventDispatcher.getInstance().detachAllMidi();
	}
}
