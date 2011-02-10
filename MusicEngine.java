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
	
	private Thread timerThread;
	private boolean isLooping;
	private SongModel songModel;
	private Map<Note,DrawableNote> notes;
	private double currentPosition;
	private Note sectionStartNote;
	private Note sectionEndNote;
	private ArrayList<Drawable> drawables;
	
	private int activeTrack;
	
	
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
		
		ArrayList<Note> noteQeue = new ArrayList<Note>();
		
		for(int i=0;i<notes.size();i++)
		{
			
			noteQeue = new ArrayList<Note>();
			
			double noteDuration = notes.get(i).getDuration();
			if(notes.get(i).getDuration()>(beatsPerMeasure-currentBeatCount))
			{
				//finish off the measure
				noteQeue.add(new Note(notes.get(i).getPitch(), (beatsPerMeasure-currentBeatCount), notes.get(i).getDynamic(), songModel.getTracks().get(activeTrack)));
				noteDuration-=(beatsPerMeasure-currentBeatCount);
				
				//check for complete measures
				while(noteDuration>=beatsPerMeasure)
				{
					noteQeue.add(new Note(notes.get(i).getPitch(), beatsPerMeasure, notes.get(i).getDynamic(), songModel.getTracks().get(activeTrack)));
					noteDuration-=beatsPerMeasure;
				}
				System.out.println(noteDuration);
				//after finishing the measure and checking for more full measures, if there is something left add it too
				if(noteDuration>0.0)
				{
					noteQeue.add(new Note(notes.get(i).getPitch(), noteDuration, notes.get(i).getDynamic(), songModel.getTracks().get(activeTrack)));
					noteDuration=0;
				}
				
				
			}
			else noteQeue.add(notes.get(i));
			
			for(int j=0;j<noteQeue.size();j++)
			{
				double noteBeat = noteQeue.get(j).getDuration();
				
				x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((currentBeatCount)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
				y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin + yOffset);
				
				
				
				
				if(noteQeue.get(j).getDynamic()==0)
				{
					//drawables.add(new WholeRest(noteQeue.get(j),x,y));
				}
				else 
				{
					if(noteBeat==0.125*beatNote)drawables.add(new EighthNote(noteQeue.get(j),x,y));
					if(noteBeat==0.25*beatNote)drawables.add(new QuarterNote(noteQeue.get(j),x,y));
					if(noteBeat==0.5*beatNote)drawables.add(new HalfNote(noteQeue.get(j),x,y));
					if(noteBeat==1*beatNote)drawables.add(new WholeNote(noteQeue.get(j),x,y));
					
					if(trebleClefNeeded)
					{
						if((restBeatStartTop!=currentBeatCount)&&(noteQeue.get(j).getPitch()>=60))
						{
							double restDurationTop = currentBeatCount-restBeatStartTop;
							
							x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartTop)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
							y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin);
							
							int restLocation=66;
							
							if(restDurationTop==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
							if(restDurationTop==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
							if(restDurationTop==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
							if(restDurationTop==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationTop, 0, songModel.getTracks().get(activeTrack)),x,y));
							
							restBeatStartTop=currentBeatCount + noteBeat;
						}
						else if(noteQeue.get(j).getPitch()>=60)restBeatStartTop = currentBeatCount + noteBeat;
					}
					
					if(bassClefNeeded)
					{
						if((restBeatStartBottom!=currentBeatCount)&&(noteQeue.get(j).getPitch()<60))
						{
							double restDurationBottom = currentBeatCount-restBeatStartBottom;
							
							x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((restBeatStartBottom)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
							y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin );
							
							int restLocation=53;
							if(!trebleClefNeeded)restLocation = 66;
							
							if(restDurationBottom==0.125*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
							if(restDurationBottom==0.25*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
							if(restDurationBottom==0.5*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
							if(restDurationBottom==1*beatNote)drawables.add(new WholeRest(new Note(restLocation, restDurationBottom, 0, songModel.getTracks().get(activeTrack)),x,(int)(y)));
							
							restBeatStartBottom=currentBeatCount + noteBeat;
						}
						else if(noteQeue.get(j).getPitch()<60)restBeatStartBottom = currentBeatCount + noteBeat;
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
							y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin );
							
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
							y = (int) (yMeasureDistance*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin);
							
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
		
		
		g.drawLine((int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance), (int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance+64));
		if(trebleClefNeeded&&bassClefNeeded)
		{
			g.drawLine((int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance)+64, (int)((xMargin) + offset -10), (int)((yMargin) + line*yMeasureDistance+194));
		}
		
	}
	
	public void play(){
		timeStarted = System.currentTimeMillis();
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
