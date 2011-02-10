package crescendo.sheetmusic;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.SongPlayer;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;

public class MusicEngine extends JPanel implements ProcessedNoteEventListener {

	private boolean isLooping;
	private SongModel songModel;
	private Map<Note,DrawableNote> notes;
	private double currentPosition;
	private Note sectionStartNote;
	private Note sectionEndNote;
	private ArrayList<Drawable> drawables;

	private int activeTrack;

	private Thread timerThread;
	private MusicEngineTimer timer;
	private boolean doContinue;
	private boolean isPaused;

	private long timeStarted;


	int measuresPerLine = 4;
	int sheetMusicWidth = 860;
	double measureWidth = sheetMusicWidth/measuresPerLine;
	double xMargin=80;
	double yMargin=80;
	double yMeasureDistance=300;
	double yOffset; //used if bass clef only
	double noteOffset = 60/measuresPerLine; //so the first note isnt on the measure line

	boolean bassClefNeeded; 
	boolean trebleClefNeeded; 

	double beatsPerMeasure;
	double beatNote;


	public MusicEngine(SongModel model,int activeTrack){
		this.setPreferredSize(new Dimension(1024, 8000));
		timerThread = new Thread(new MusicEngineTimer());
		isLooping = false;
		songModel = model;
		notes = new HashMap<Note,DrawableNote>();
		drawables = new ArrayList<Drawable>();
		currentPosition = 0.0;
		sectionStartNote = null;
		sectionEndNote = null;

		this.activeTrack = activeTrack;


		timeStarted=0;

		timer = new MusicEngineTimer();
		timerThread = new Thread(timer);
		doContinue = true;



		//Get our drawables ready
		LinkedList<Note> notes =(LinkedList<Note>) songModel.getTracks().get(activeTrack).getNotes();

		System.out.println("Notes : " + notes.size() + " \n");





		beatsPerMeasure = songModel.getTimeSignature().getBeatsPerMeasure();
		beatNote = songModel.getTimeSignature().getBeatNote();



		double currentBeatCount = 0;
		double currentMeasure=0;

		double restBeatStartTop=0;
		double restBeatStartBottom=0;


		int x=0;
		int y=0;


		//PREPROCESS 1: Remove Base/Treble Clef if unused
		bassClefNeeded = false; 
		trebleClefNeeded = false; 
		for(int i=0;i<notes.size();i++)
		{
			if(notes.get(i).getDynamic()>0)
			{
				if(notes.get(i).getPitch()>=60)trebleClefNeeded = true;
				if(notes.get(i).getPitch()<60)bassClefNeeded = true;
			}
		}

		yOffset=0;
		if((trebleClefNeeded)&&(!bassClefNeeded))
		{
			yMeasureDistance=150;
		}
		if((bassClefNeeded)&&(!trebleClefNeeded))
		{
			yMeasureDistance=150;
			yOffset = -130;
		}


		for(int i=0;i<notes.size();i++)
		{
			double noteBeat = notes.get(i).getDuration();



			x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((currentBeatCount)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
			y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);




			if(notes.get(i).getDynamic()==0)
			{
				//drawables.add(new WholeRest(notes.get(i),x,y));
			}
			else 
			{
				if(noteBeat==0.125*beatNote)drawables.add(new EighthNote(notes.get(i),x,y));
				if(noteBeat==0.25*beatNote)drawables.add(new QuarterNote(notes.get(i),x,y));
				if(noteBeat==0.5*beatNote)drawables.add(new HalfNote(notes.get(i),x,y));
				if(noteBeat==1*beatNote)drawables.add(new WholeNote(notes.get(i),x,y));

				if(trebleClefNeeded)
				{
					if((restBeatStartTop!=currentBeatCount)&&(notes.get(i).getPitch()>=60))
					{
						double restDurationTop = currentBeatCount-restBeatStartTop;

						x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartTop)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
						y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);

						int restLocation=66;

						if(restDurationTop==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));

						restBeatStartTop=currentBeatCount + noteBeat;
					}
					else if(notes.get(i).getPitch()>=60)restBeatStartTop = currentBeatCount + noteBeat;
				}

				if(bassClefNeeded)
				{
					if((restBeatStartBottom!=currentBeatCount)&&(notes.get(i).getPitch()<60))
					{
						double restDurationBottom = currentBeatCount-restBeatStartBottom;

						x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartBottom)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
						y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);

						int restLocation=53;
						if(!trebleClefNeeded)restLocation = 66;

						if(restDurationBottom==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));

						restBeatStartBottom=currentBeatCount + noteBeat;
					}
					else if(notes.get(i).getPitch()<60)restBeatStartBottom = currentBeatCount + noteBeat;
				}
			}



			currentBeatCount+=noteBeat;
			while(currentBeatCount>=beatsPerMeasure)
			{

				if(trebleClefNeeded)
				{
					if(restBeatStartTop!=currentBeatCount)
					{
						double restDurationTop = currentBeatCount-restBeatStartTop;

						x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartTop)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
						y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);

						int restLocation=66;

						if(restDurationTop==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
						if(restDurationTop==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));

					}
				}

				if(bassClefNeeded)
				{
					if(restBeatStartBottom!=currentBeatCount)
					{
						double restDurationBottom = currentBeatCount-restBeatStartBottom;

						x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartBottom)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
						y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);

						int restLocation=53;
						if(!trebleClefNeeded)restLocation = 66;

						if(restDurationBottom==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
						if(restDurationBottom==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));

					}
				}


				currentBeatCount-=beatsPerMeasure;
				currentMeasure++;

				restBeatStartTop=0;
				restBeatStartBottom=0;


			}
		}
	}

	public MusicEngine(SongModel model,int activeTrack, double currentPosition){
		this(model,activeTrack);
		this.currentPosition = currentPosition;
		//use logic for finding the number of beats in a song
	}

	public MusicEngine(SongModel model,int activeTrack, Note currentNote){
		this(model,activeTrack);
		this.sectionStartNote = currentNote;
	}

	public MusicEngine(SongModel model,int activeTrack, Note currentSectionBeginNote, Note currentSectionEndNote){
		this(model,activeTrack);
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
		super.paint(g);


		//draw staffs
		for(int j=0;j<20;j++)
		{	
			//g.drawLine((int)xMargin,(int)(yMargin+(yMeasureDistance*j)),(int)(xMargin),(int)(yMargin+194+(yMeasureDistance*j)));



			//draw top staff lines
			for(int i=0;i<5;i++)
				g.drawLine((int)(xMargin),(int)(yMargin+(16*i)+(yMeasureDistance*j)),(int)(xMargin+measureWidth*measuresPerLine),(int)(yMargin+(16*i)+(yMeasureDistance*j)));
			//draw top measure lines
			for(int k=0;k<=measuresPerLine;k++)
				g.drawLine((int)(xMargin+measureWidth*k),(int)(yMargin+(yMeasureDistance*j)),(int)(xMargin+measureWidth*k),(int)(yMargin+64+(yMeasureDistance*j)));

			//draw bottom staff if needed 
			if(trebleClefNeeded&&bassClefNeeded)
			{
				//draw bottom staff lines
				for(int i=0;i<5;i++)
					g.drawLine((int)(xMargin),(int)(yMargin+130+(16*i)+(yMeasureDistance*j)),(int)(xMargin+measureWidth*measuresPerLine),(int)(yMargin+130+(16*i)+(yMeasureDistance*j)));
				//draw bottom measure lines
				for(int k=0;k<=measuresPerLine;k++)
					g.drawLine((int)(xMargin+measureWidth*k),(int)(yMargin+64+(yMeasureDistance*j)),(int)(xMargin+measureWidth*k),(int)(yMargin+194+(yMeasureDistance*j)));
			}
		}



		for(Drawable d: drawables)d.draw(g);



		double timeDelta = System.currentTimeMillis() - timeStarted;
		double linePerMS = ((double)songModel.getBPM())/(beatsPerMeasure*((double)measuresPerLine))/60.0/1000.0;

		double line = Math.floor(timeDelta*linePerMS);
		double offset = ((timeDelta-(line/linePerMS))*linePerMS)*measureWidth*measuresPerLine;
		g.setColor(Color.red);

		g.drawLine((int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance), (int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance+194));

	}

	public void play(){
		timeStarted = System.currentTimeMillis();
		isPaused = false;
		timerThread.start();
	}

	public void pause(){
		isPaused = true;
	}

	public void stop(){
		doContinue=false;
	}

	public void resume(){
		isPaused = false;
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

		private final int FRAMES_PER_SECOND = 300;

		private final double MS_DELAY=1000.0/FRAMES_PER_SECOND;
		/** number of milliseconds from the epoch of when the last frame started */
		private long lastFrame = 0;

		/**
		 * Method which get called by the thread, this is running while the song is playing or paused
		 */
		@Override
		public void run() {
			while(doContinue) {
				long now = System.currentTimeMillis();
				if(!isPaused) {
					if(now > (lastFrame + MS_DELAY)) {
						repaint();
						lastFrame = now;	//We want to run at FRAMES_PER_SECOND fps, so use the beginning of the frame to
						//ensure that we get the correct frames, no matter how long update takes
					} else {
						try {
							Thread.sleep(1); // Dont eat up all the processor
						} 
						catch (InterruptedException e) {}
					}
				}else{
					try {
						Thread.sleep(1); // Dont eat up all the processor
					} 
					catch (InterruptedException e) {}
				}
			}
		}
	}

}