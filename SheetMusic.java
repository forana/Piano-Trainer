package crescendo.sheetmusic;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import crescendo.base.AudioPlayer;
import crescendo.base.ErrorHandler;
import crescendo.base.ErrorHandler.Response;
import crescendo.base.HeuristicsModel;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.GUI.PianoTrainerApplication;
import crescendo.base.module.Module;
import crescendo.base.song.SongFactory;
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
		
		this.setLayout(new BorderLayout());
		
		add(mainAreaTarget,BorderLayout.CENTER);
		add(bottomBarContainer,BorderLayout.SOUTH);
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
	}

	public void loadSong(String filename){
		loadedSongPath = filename;
		SongModel selectedSongModel = null;
		int maxRetrys = 10;
		int currentRetrys = 0;
		try {
			while(selectedSongModel==null && currentRetrys<maxRetrys){
				selectedSongModel = SongFactory.generateSongFromFile(filename);
			}
		} catch (IOException e) {
			Response response = ErrorHandler.showRetryFail("Could not load...", "Could not load song in file "+filename+". Would you like to try again?");
			if(response==Response.RETRY){
				currentRetrys++;
			}else{
				currentRetrys+=maxRetrys;
			}
		}
		//don't continue if the song didnt load
		
		//Show select current track dialog or figure out the preferred active track
		//TODO: This is a temporary fix, ask the user to manually input the track number until they get a valid track number
		int activeTrack = 1;
		while(activeTrack < 1 || activeTrack > selectedSongModel.getTracks().size()){
			try{
				activeTrack = Integer.parseInt(JOptionPane.showInputDialog(this, "What track would you like to play? (1 - "+selectedSongModel.getTracks().size()+")"));
		
			}catch(NumberFormatException formatException){
				activeTrack = -1;
			}
		}
		activeTrack-=1;
		//End work-around
		
		// Initialize meta-things
		boolean careAboutPitch=true;
		boolean careAboutDynamic=true;
		HeuristicsModel heuristics=new HeuristicsModel(careAboutPitch,careAboutDynamic);
		
		//Hook up song processor pieces
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		songPlayer = new SongPlayer(selectedSongModel);
		SongValidator validator = new SongValidator(selectedSongModel,selectedSongModel.getTracks().get(activeTrack),heuristics);
		AudioPlayer audioPlayer = new AudioPlayer(selectedSongModel, selectedSongModel.getTracks().get(activeTrack));
		
		//Initialize UI Pieces
		adviceFeedbackFrame = new AdviceFrame(heuristics,songPlayer.getSongState());
		scoreFeedbackFrame = new ScoreFrame(new ScoreCalculator(careAboutPitch,careAboutDynamic,songPlayer.getSongState(),heuristics));
		musicEngine = new MusicEngine(selectedSongModel);
		
		bottomBarContainer.add(adviceFeedbackFrame);
		//bottomBarContainer.setVisible(true);
		
		mainAreaTarget.setViewportView(musicEngine);
		//mainAreaTarget.setVisible(true);
		
		add(scoreFeedbackFrame,BorderLayout.NORTH);
		
		//Add the progress frame to the bottom bar container...
		
		//Attach input events
		dispatcher.attach(validator);
		
		//Attach note events
		songPlayer.attach(audioPlayer, (int)audioPlayer.getLatency());
		songPlayer.attach(validator,50); // TODO base this number in the heuristics model
		
		//Attach flow controllers
		songPlayer.attach(audioPlayer);
		songPlayer.attach(validator);
		
		//Attach processed note events
		validator.attach(adviceFeedbackFrame);
		validator.attach(scoreFeedbackFrame);
		validator.attach(musicEngine);
	
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
