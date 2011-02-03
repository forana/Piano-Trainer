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
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.GUI.PianoTrainerApplication;
import crescendo.base.module.Module;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SheetMusic extends Module{

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
		
		this.setSize(1024, 768);
		
		
		
		bottomBarContainer = new JPanel();
		bottomBarContainer.setVisible(true);
		
		
		
		mainAreaTarget = new JScrollPane();
		mainAreaTarget.setSize(1024, 500);
		mainAreaTarget.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainAreaTarget.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainAreaTarget.getHorizontalScrollBar().addAdjustmentListener(MyAdjustmentListener);
		mainAreaTarget.getVerticalScrollBar().addAdjustmentListener(MyAdjustmentListener);
		mainAreaTarget.setVisible(true);
		
		this.setLayout(new BorderLayout());
		
		add(mainAreaTarget,BorderLayout.CENTER);
		add(bottomBarContainer,BorderLayout.SOUTH);
	}
	
	AdjustmentListener MyAdjustmentListener = new AdjustmentListener(){
	    // This method is called whenever the value of a scrollbar is changed,
	    // either by the user or programmatically.
	    public void adjustmentValueChanged(AdjustmentEvent evt) {
	    	musicEngine.repaint();
	    	SheetMusic.this.repaint();
	    	mainAreaTarget.repaint();
	    	bottomBarContainer.repaint();
	    	SheetMusic.this.invalidate();
	    	musicEngine.invalidate();
	    	mainAreaTarget.invalidate();
	    	
	    	
	    	System.out.println("imathing");
	        Adjustable source = evt.getAdjustable();

	        // getValueIsAdjusting() returns true if the user is currently
	        // dragging the scrollbar's knob and has not picked a final value
	        if (evt.getValueIsAdjusting()) {
	        	
	            return;
	        }

	        // Determine which scrollbar fired the event
	        int orient = source.getOrientation();
	        if (orient == Adjustable.HORIZONTAL) {
	            // Event from horizontal scrollbar
	        } else {
	            // Event from vertical scrollbar
	        }

	        // Determine the type of event
	        int type = evt.getAdjustmentType();
	        switch (type) {
	          case AdjustmentEvent.UNIT_INCREMENT:
	              // Scrollbar was increased by one unit
	              break;
	          case AdjustmentEvent.UNIT_DECREMENT:
	              // Scrollbar was decreased by one unit
	              break;
	          case AdjustmentEvent.BLOCK_INCREMENT:
	              // Scrollbar was increased by one block
	              break;
	          case AdjustmentEvent.BLOCK_DECREMENT:
	              // Scrollbar was decreased by one block
	              break;
	          case AdjustmentEvent.TRACK:
	              // The knob on the scrollbar was dragged
	              break;
	        }

	        // Get current value
	        int value = evt.getValue();
	    }
	};

	
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
		
		
		//Initialize UI Pieces
		adviceFeedbackFrame = new AdviceFrame();
		scoreFeedbackFrame = new ScoreFrame(selectedSongModel.getTracks().get(activeTrack));
		musicEngine = new MusicEngine(selectedSongModel);
	
		
		bottomBarContainer.add(adviceFeedbackFrame);
		bottomBarContainer.setVisible(true);
		
		mainAreaTarget.setViewportView(musicEngine);
		mainAreaTarget.setVisible(true);
		
		add(scoreFeedbackFrame,BorderLayout.NORTH);
		
		//Add the progress frame to the bottom bar container...
		
		
		//Hook up song processor pieces
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		songPlayer = new SongPlayer(selectedSongModel);
		SongValidator validator = new SongValidator();
		AudioPlayer audioPlayer = new AudioPlayer(selectedSongModel, selectedSongModel.getTracks().get(activeTrack));
		
		
		//Attach input events
		dispatcher.attach(validator);
		
		//Attach note events
		songPlayer.attach(audioPlayer, (int)audioPlayer.getLatency());
		songPlayer.attach(validator,50); //base this number in the huristics model
		
		//Attach flow controllers
		songPlayer.attach(audioPlayer);
		songPlayer.attach(validator);
		
		//Attach processed note events
		//validator.attach(adviceFeedbackFrame);
		//validator.attach(scoreFeedbackFrame);
		//validator.attach(musicEngine);
	
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
