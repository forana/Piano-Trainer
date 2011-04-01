package crescendo.game;

//import javax.swing.JComponent;
//import crescendo.base.HeuristicsModel;
//import crescendo.base.SongValidator;
import crescendo.base.module.Module;
//import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	private String loadedSongPath;

	//private JComponent display;
	//private GameEngine gameEngine;
	//private SongValidator songValidator;
	
	public GameModule() {
		this.showSongSelectionPanel();
	}
	
	public void showGamePanel(SongModel model,Track activeTrack) {
		this.removeAll();
		this.add(new GameEngine(model,activeTrack));
		//songValidator = new SongValidator(model,activeTrack,new HeuristicsModel(ProfileManager.getInstance().getActiveProfile().getIsPitchGraded(), ProfileManager.getInstance().getActiveProfile().getIsDynamicGraded()));
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
	
	public void play(){
		//gameEngine.play();
	}
	
	public void pause(){
		//gameEngine.pause();
	}
	
	public void resume(){
		//gameEngine.resume();
	}
	
	public void stop(){
		//gameEngine.stop();
	}
	
	public void songEnd(){
		//gameEngine.songEnd();
		//songValidator.songEnd();
	}
	
	@Override
	public String saveState() {
		return loadedSongPath;
	}

	@Override
	public void cleanUp() {
		try{
			songEnd();
		}catch(NullPointerException e){
		}
		
	}
}
