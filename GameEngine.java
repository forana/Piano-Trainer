package crescendo.game;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import crescendo.base.HeuristicsModel;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.GUI.ProfileModule;
import crescendo.base.profile.ProfileManager;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameEngine extends JPanel{
	private static final long serialVersionUID=1L;
	
	//The panels that make up this Screen
	private GameGraphicsPanel graphicsPanel;
	private GameScorePanel scorePanel;
	
	private JButton playButton;
	private JButton stopButton;
	
	/*
	 * GameEngine
	 * 
	 * This class is made up of two main parts: the score frame at the top,
	 * and the graphics of the game itself.
	 * 
	 * @param model - A reference the the songmodel to work from
	 * @param activeTrack - the active track to display the notes of
	 */
	public GameEngine(GameModule module,SongModel model,List<Track> activeTracks,List<Track> audioTracks) {
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		HeuristicsModel heuristics=new HeuristicsModel(ProfileManager.getInstance().getActiveProfile().getIsPitchGraded(),
				ProfileManager.getInstance().getActiveProfile().getIsPitchGraded());
				
		SongPlayer player=new SongPlayer(model);

		SongValidator validator=new SongValidator(model,activeTracks,heuristics);
		
		ScoreCalculator calc=new ScoreCalculator(heuristics.listeningPitch(),heuristics.listeningVelocity(),player.getSongState(),heuristics);
		
		
		scorePanel = new GameScorePanel(module,model,activeTracks,audioTracks,calc);
		graphicsPanel = new GameGraphicsPanel(model,activeTracks,audioTracks,player);
		
		player.attach(scorePanel);
		player.attach(validator,(int)(heuristics.getTimingInterval()/player.getSongState().getBPM()*60000)/2);
		validator.attach(scorePanel);
		
		validator.attach(graphicsPanel);
		EventDispatcher.getInstance().attach(graphicsPanel);
		EventDispatcher.getInstance().attach(validator);
		
		this.add(scorePanel);
		this.add(graphicsPanel);
		
		playButton = new JButton("Play");
		playButton.addActionListener(al);
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(al);
		
		this.add(playButton);
		this.add(stopButton);
	}
	
	public void stop()
	{
		graphicsPanel.stop();
	}
	
	public void play()
	{
		graphicsPanel.play();
	}
	
	
	private ActionListener al = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			
			
			if(e.getSource().equals(playButton))
			{
				GameEngine.this.play();
			}
			
			if(e.getSource().equals(stopButton))
			{
				GameEngine.this.stop();
			}
		}
	};
}