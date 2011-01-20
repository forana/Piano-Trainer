package crescendo.sheetmusic;

import java.awt.Canvas;
import java.awt.Graphics;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;

public class MusicEngine extends Canvas implements ProcessedNoteEventListener {
	
	private Thread timerThread;
	private boolean isLooping;
	private SongModel songModel;
	//private Map<Note,DrawableNote> notes;
	private double currentPosition;
	private Note sectionStartNote;
	private Note sectionEndNote;
	//private List<Drawable> drawables;
	

	public MusicEngine(SongModel model){
		timerThread = new Thread(new MusicEngineTimer());
		isLooping = false;
		songModel = model;
		//notes = new HashMap<Note,MetaNote>();
		currentPosition = 0.0;
		sectionStartNote = null;
		sectionEndNote = null;
	}

	public MusicEngine(SongModel model, double currentPosition){
		this(model);
		this.currentPosition = currentPosition;
		//use logic for finding the number of beats in a song
	}
	
	public MusicEngine(SongModel model, Note currentNote){
		this(model);
		this.sectionStartNote = currentNote;
	}
	
	public MusicEngine(SongModel model, Note currentSectionBeginNote, Note currentSectionEndNote){
		this(model);
		sectionStartNote = currentSectionBeginNote;
		sectionEndNote = currentSectionEndNote;
	}
	
	public void setLooping(boolean looping){
		isLooping = looping;
	}
	
	public void setPosition(double position){
		currentPosition = position;
	}
	
	public void setPosition(Note position){
		sectionStartNote = position;
	}
	
	public void setSection(Note sectionStartNote, Note sectionEndNote){
		this.sectionStartNote = sectionStartNote;
		this.sectionEndNote = sectionEndNote;
	}
	
	@Override
	public void paint(Graphics g){

	}
	
	public void play(){
		
	}
	
	public void pause(){
		
	}
	
	public void stop(){
		
	}
	
	public void resume(){
		
	}
	
	@Override
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		if(e.getExpectedNote()==null){
			//Create a new incorrect note
		}else{
			//notes.get(e.getExpectedNote()).setCorrect(e.isCorrect());
		}
	}
	
	private class MusicEngineTimer implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
