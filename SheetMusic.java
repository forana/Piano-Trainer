package crescendo.sheetmusic;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import crescendo.base.AudioPlayer;
import crescendo.base.HeuristicsModel;
import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class SheetMusic extends Module{
	private static final long serialVersionUID = 1L;

	private String loadedSongPath;	//Save path for resuming after invalid shutdown
	
	//private FlowController flowController;
	private AdviceFrame adviceFeedbackFrame;
	private MusicEngine musicEngine;
	private ScoreFrame scoreFeedbackFrame;
	private AudioPlayer audioPlayer;
	private SongPlayer songPlayer;
	
	private ScoreCalculator score;
	private SongModel selectedSongModel;
	private Track activeTrack;
	
	private JPanel bottomBarContainer;
	
	private JScrollPane mainAreaTarget;

	
	public SheetMusic(){
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
		mainAreaTarget.setViewportView(new SongSelectionScreen(this));
	}
	
	public void showTrackSelectionScreen(SongModel model) {
		mainAreaTarget.setViewportView(new TrackSelectionScreen(this,model));
	}
	
	public void showScore() {
		this.remove(scoreFeedbackFrame);
		this.remove(bottomBarContainer);
		this.updateUI();
		mainAreaTarget.setViewportView(new ScoreDisplay(this,score,selectedSongModel,activeTrack));
	}

	public void loadSong(SongModel model,Track activeTrack){
	
		selectedSongModel = model;
		this.activeTrack=activeTrack;
		
		// Initialize meta-things
		boolean careAboutPitch=true;
		boolean careAboutDynamic=false;
		HeuristicsModel heuristics=new HeuristicsModel(careAboutPitch,careAboutDynamic);
		
		//Hook up song processor pieces
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		songPlayer = new SongPlayer(selectedSongModel);
		SongValidator validator = new SongValidator(selectedSongModel,activeTrack,heuristics);
		audioPlayer = new AudioPlayer(selectedSongModel, null /*selectedSongModel.getTracks().get(activeTrack)*/);//TODO make this be the actual active track (uncomment and remove the null)
		
		//Initialize UI Pieces
		adviceFeedbackFrame = new AdviceFrame(heuristics,songPlayer.getSongState());
		score=new ScoreCalculator(careAboutPitch,careAboutDynamic,songPlayer.getSongState(),heuristics);
		scoreFeedbackFrame = new ScoreFrame(score);
		musicEngine = new MusicEngine(selectedSongModel,0); // TODO pass the track reference
		bottomBarContainer = new JPanel();
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
		//add listener for song's end
		songPlayer.attach(new ScoreDisplayListener(this));
		
		//Attach processed note events
		validator.attach(adviceFeedbackFrame);
		validator.attach(scoreFeedbackFrame);
		validator.attach(musicEngine);
		this.updateUI();
		
		
		EventDispatcher.getInstance().registerComponent(musicEngine);
	}
	
	public void play(){
		playIntro();
		songPlayer.play();
		musicEngine.play();
	}
	
	public void pause(){
		songPlayer.pause();
		musicEngine.pause();
	}
	
	public void resume(){
		playIntro();
		songPlayer.resume();
		musicEngine.resume();
	}
	
	public void stop(){
		if (songPlayer!=null)
		{
			songPlayer.stop();
		}
		if (musicEngine!=null)
		{
			musicEngine.stop();
		}
	}
	
	private void playIntro(){
		Note n = new Note(60, 1, 90, audioPlayer.getMetronomeTrack());
		NoteEvent bne = new NoteEvent(n, NoteAction.BEGIN,0 );
		NoteEvent ene = new NoteEvent(n, NoteAction.BEGIN,0 );
		for(int i=0;i<songPlayer.getSongState().getTimeSignature().getBeatsPerMeasure();i++){
			try {
				audioPlayer.handleNoteEvent(bne);
				Thread.sleep(10);
				audioPlayer.handleNoteEvent(ene);
				System.out.println();
				Thread.sleep((int)1000/(songPlayer.getSongState().getBPM()/60));
			} catch (InterruptedException e) {}
		}
	}


	@Override
	public String saveState() {
		return loadedSongPath;
	}

	@Override
	public void cleanUp() {
		stop();
	}
}
