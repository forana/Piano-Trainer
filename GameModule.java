package crescendo.game;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import crescendo.base.AudioPlayer;
import crescendo.base.HeuristicsModel;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class GameModule extends Module {
	private static final long serialVersionUID=1L;
	
	private String loadedSongPath;

	private JComponent display;
	private GameEngine gameEngine;
	private SongValidator songValidator;

	
	public void showGamePanel(SongModel model, Track activeRef) {
		this.removeAll();
		
		
		songValidator = new SongValidator(model,activeRef,new HeuristicsModel(ProfileManager.getInstance().getActiveProfile().getIsPitchGraded(), ProfileManager.getInstance().getActiveProfile().getIsDynamicGraded()));
		gameEngine = new GameEngine(this,model);
	
		
		this.add(gameEngine);
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
	
	public void loadSong(SongModel model, int activeTrack){
		
		SongModel selectedSongModel = model;
		
		
		//Hook up song processor pieces
		EventDispatcher dispatcher = EventDispatcher.getInstance();
			
		gameEngine = new GameEngine(this,selectedSongModel);


		display.add(gameEngine);


		this.updateUI();
		
		
		EventDispatcher.getInstance().registerComponent(gameEngine);
	}
	
	public void play(){
		gameEngine.play();
	}
	
	public void pause(){
		gameEngine.pause();
	}
	
	public void resume(){
		gameEngine.resume();
	}
	
	public void stop(){
		gameEngine.stop();
	}
	
	public void songEnd(){
		gameEngine.songEnd();
		songValidator.songEnd();
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
