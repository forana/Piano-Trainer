package crescendo.sheetmusic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import crescendo.base.AudioPlayer;
import crescendo.base.HeuristicsModel;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
import crescendo.base.song.SongModel;

public class SheetMusic extends Module{
	private static final long serialVersionUID = 1L;

	private String loadedSongPath;	//Save path for resuming after invalid shutdown
	
	//private FlowController flowController;
	private AdviceFrame adviceFeedbackFrame;
	private MusicEngine musicEngine;
	private ScoreFrame scoreFeedbackFrame;
	
	private SongPlayer songPlayer;
	
	private JPanel bottomBarContainer;
	
	private JScrollPane mainAreaTarget;
	
	public SheetMusic(){
		//TODO:Load up the UI
		//this.setSize(1024, 768);
		
		bottomBarContainer = new JPanel();
		//bottomBarContainer.setVisible(true);
		
		mainAreaTarget = new JScrollPane();
		//mainAreaTarget.setSize(1024, 500);
		mainAreaTarget.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainAreaTarget.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//mainAreaTarget.setVisible(true);
		
		showSongSelectionScreen();
		
		this.setLayout(new BorderLayout());
		
		add(mainAreaTarget,BorderLayout.CENTER);
		mainAreaTarget.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
	}
	
	/**
	 * Load the sheet music module with a previously saved state
	 * @param saved state information
	 */
	public SheetMusic(String state){
		this();
		
		//load up the state information

	}
	
	public void showSongSelectionScreen(){
		//load the song selection screen
		mainAreaTarget.setViewportView(new SongSelectionScreen(this,600,100));
	}

	public void loadSong(SongModel model, int activeTrack){
	
		SongModel selectedSongModel = model;
		
		// Initialize meta-things
		boolean careAboutPitch=true;
		boolean careAboutDynamic=true;
		HeuristicsModel heuristics=new HeuristicsModel(careAboutPitch,careAboutDynamic);
		
		//Hook up song processor pieces
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		songPlayer = new SongPlayer(selectedSongModel);
		SongValidator validator = new SongValidator(selectedSongModel,selectedSongModel.getTracks().get(activeTrack),heuristics);
		AudioPlayer audioPlayer = new AudioPlayer(selectedSongModel, null /*selectedSongModel.getTracks().get(activeTrack)*/);//TODO make this be the actual active track (uncomment and remove the null)
		
		//Initialize UI Pieces
		adviceFeedbackFrame = new AdviceFrame(heuristics,songPlayer.getSongState());
		scoreFeedbackFrame = new ScoreFrame(new ScoreCalculator(careAboutPitch,careAboutDynamic,songPlayer.getSongState(),heuristics));
		musicEngine = new MusicEngine(selectedSongModel,activeTrack);
		bottomBarContainer.setLayout(new GridLayout(1,2));
		FlowControllerBar bar = new FlowControllerBar(0, 0, 500, 50, this,model);
		bottomBarContainer.add(bar);
		bottomBarContainer.add(adviceFeedbackFrame);		
		
		mainAreaTarget.setViewportView(musicEngine);

		bottomBarContainer.setSize(bottomBarContainer.getWidth(), 200);
		
		add(scoreFeedbackFrame,BorderLayout.NORTH);
		add(bottomBarContainer,BorderLayout.SOUTH);
	
		
		//Add the progress frame to the bottom bar container...
		
		
		//Attach input events
		dispatcher.attach(validator);
		
		//Attach note events
		songPlayer.attach(audioPlayer, (int)audioPlayer.getLatency());
		songPlayer.attach(validator,(int)(heuristics.getTimingInterval()/songPlayer.getSongState().getBPM()*60000)/2);
		songPlayer.attach(bar,20);
		
		//Attach flow controllers
		songPlayer.attach(audioPlayer);
		songPlayer.attach(validator);
		
		//Attach processed note events
		validator.attach(adviceFeedbackFrame);
		validator.attach(scoreFeedbackFrame);
		validator.attach(musicEngine);
		this.updateUI();
	}
	
	public void play(){
		songPlayer.play();
		musicEngine.play();
	}
	
	public void pause(){
		songPlayer.pause();
		musicEngine.pause();
	}
	
	public void resume(){
		songPlayer.resume();
		musicEngine.resume();
	}
	
	public void stop(){
		songPlayer.stop();
		musicEngine.stop();
	}


	@Override
	public String saveState() {
		return loadedSongPath;
	}

	@Override
	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

}
