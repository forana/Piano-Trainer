package crescendo.module.sheetmusic;

import java.io.IOException;
import javax.swing.JOptionPane;
import crescendo.base.AudioPlayer;
import crescendo.base.ErrorHandler;
import crescendo.base.ErrorHandler.Response;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.base.module.Module;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SheetMusic extends Module{

	private String loadedSongPath;	//Save path for resuming after invalid shutdown
	
	//private FlowController flowController;
	//private Feedback feedback;
	//private SongEngine songEngine;
	
	
	public SheetMusic(){
		//TODO:Load up the UI  
	}
	
	/**
	 * Load the sheet music module with a previously saved state
	 * @param saved state information
	 */
	public SheetMusic(String state){
		this();
		
		//load up the state information

	}

	public void loadSong(String filename){
		loadedSongPath = filename;
		SongModel selectedSongModel = null;
		int maxRetrys = 10;
		int currentRetrys = 0;
		try {
			while(selectedSongModel!=null && currentRetrys<maxRetrys){
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
		int activeTrack = -1;
		while(activeTrack < 1 || activeTrack > selectedSongModel.getTracks().size()){
			try{
				activeTrack = Integer.parseInt(JOptionPane.showInputDialog(this, "What track would you like to play? (1 - "+selectedSongModel.getTracks().size()+")"));
		
			}catch(NumberFormatException formatException){
				activeTrack = -1;
			}
		}
		activeTrack-=1;
		//End work-around
		
		
		//Hook up song processor peices
		EventDispatcher dispatcher = EventDispatcher.getInstance();
		SongPlayer songPlayer = new SongPlayer(selectedSongModel);
		SongValidator validator = new SongValidator();
		AudioPlayer audioPlayer = new AudioPlayer(selectedSongModel, selectedSongModel.getTracks().get(activeTrack));
		
		//Attach input events
		dispatcher.attach(validator);
		
		//Attach note events
		songPlayer.attach(audioPlayer, (int)audioPlayer.getLatency());
		songPlayer.attach(validator,50); //base this number in the huristics model
			//song engine
		
		//Attach flow controllers
		songPlayer.attach(audioPlayer);
		songPlayer.attach(validator);
		
		//Attach processed note events
			//feedback
			//song engine
	
	}



	@Override
	public String saveState() {
		return loadedSongPath;
	}

}
