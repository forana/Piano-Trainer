package crescendo.game;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import crescendo.base.HeuristicsModel;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameEngine extends JPanel{
	private static final long serialVersionUID=1L;
	
	//The panels that make up this Screen
	private GameGraphicsPanel graphicsPanel;
	private GameScorePanel scorePanel;
	
	
	
	/*
	 * GameEngine
	 * 
	 * This class is made up of two main parts: the score frame at the top,
	 * and the graphics of the game itself.
	 * 
	 * @param model - A reference the the songmodel to work from
	 * @param activeTrack - the active track to display the notes of
	 */
	public GameEngine(GameModule module,SongModel model,Track activeTrack,List<Track> audioTracks) {
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		HeuristicsModel heuristics=new HeuristicsModel(ProfileManager.getInstance().getActiveProfile().getIsPitchGraded(),
				ProfileManager.getInstance().getActiveProfile().getIsPitchGraded());
				
		SongPlayer player=new SongPlayer(model);
		SongValidator validator=new SongValidator(model,activeTrack,heuristics);
		
		ScoreCalculator calc=new ScoreCalculator(heuristics.listeningPitch(),heuristics.listeningVelocity(),player.getSongState(),heuristics);
		
		
		scorePanel = new GameScorePanel(module,model,activeTrack,audioTracks,calc);
		graphicsPanel = new GameGraphicsPanel(model,activeTrack,audioTracks,player);
		
		player.attach(scorePanel);
		player.attach(validator,(int)(heuristics.getTimingInterval()/player.getSongState().getBPM()*60000)/2);
		validator.attach(scorePanel);
		
		validator.attach(graphicsPanel);
		EventDispatcher.getInstance().attach(graphicsPanel);
		EventDispatcher.getInstance().attach(validator);
		
		this.add(scorePanel);
		this.add(graphicsPanel);
	}
}