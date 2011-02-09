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
	double noteOffset = 60/measuresPerLine; //so the first note isnt on the measure line
	
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
		
		int x=0;
		int y=0;
		
		for(int i=0;i<notes.size();i++)
		{
			double noteBeat = notes.get(i).getDuration();
			
//			if(notes.get(i).getDynamic()==0)pitch = 66;
			
			x = (int) ((currentMeasure%measuresPerLine * measureWidth) + (((currentBeatCount)/beatsPerMeasure)*measureWidth) + xMargin + noteOffset);
			y = (int) (300*((currentMeasure-(currentMeasure%measuresPerLine))/measuresPerLine) + yMargin);
			
			
			
			/************************** TRYME ************************************/
			//if a "beat" is a half note
			
			if(notes.get(i).getDynamic()==0);//drawables.add(new Rest(notes.get(i),x));
			else 
			{
				if(noteBeat==0.125*beatNote)drawables.add(new EighthNote(notes.get(i),x,y));
				if(noteBeat==0.25*beatNote)drawables.add(new QuarterNote(notes.get(i),x,y));
				if(noteBeat==0.5*beatNote)drawables.add(new HalfNote(notes.get(i),x,y));
				if(noteBeat==1*beatNote)drawables.add(new WholeNote(notes.get(i),x,y));
			}
			
			/***********************************************************************/
		
			
			//if a "beat" is a half note
			/*if(beatNote==2)
			{
				if(notes.get(i).getDynamic()==0);//drawables.add(new Rest(notes.get(i),x));
				else 
				{
					if(noteBeat==0.25)drawables.add(new EighthNote(notes.get(i),x,y));
					if(noteBeat==0.5)drawables.add(new QuarterNote(notes.get(i),x,y));
					if(noteBeat==1)drawables.add(new HalfNote(notes.get(i),x,y));
					if(noteBeat==2)drawables.add(new WholeNote(notes.get(i),x,y));
				}
			}
			//if a "beat" is a quarter note
			else if(beatNote==4)
			{
				if(notes.get(i).getDynamic()==0);//drawables.add(new Rest(notes.get(i),x));
				else 
				{
					if(noteBeat==0.5)drawables.add(new EighthNote(notes.get(i),x,y));
					if(noteBeat==1)drawables.add(new QuarterNote(notes.get(i),x,y));
					if(noteBeat==2)drawables.add(new HalfNote(notes.get(i),x,y));
					if(noteBeat==4)drawables.add(new WholeNote(notes.get(i),x,y));
				}
			}
			//if a "beat" is a eighth note
			else if(beatNote==8)
			{
				if(notes.get(i).getDynamic()==0);//drawables.add(new Rest(notes.get(i),x));
				else 
				{
					if(noteBeat==1)drawables.add(new EighthNote(notes.get(i),x,y));
					if(noteBeat==2)drawables.add(new QuarterNote(notes.get(i),x,y));
					if(noteBeat==4)drawables.add(new HalfNote(notes.get(i),x,y));
					if(noteBeat==8)drawables.add(new WholeNote(notes.get(i),x,y));
				}
			}*/
			
			currentBeatCount+=noteBeat;
			while(currentBeatCount>=beatsPerMeasure)
			{
				currentBeatCount-=beatsPerMeasure;
				currentMeasure++;
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
			g.drawLine((int)xMargin,(int)(yMargin+(yMeasureDistance*j)),(int)(xMargin),(int)(yMargin+194+(yMeasureDistance*j)));
			for(int k=1;k<=measuresPerLine;k++)
				g.drawLine((int)(xMargin+measureWidth*k),(int)(yMargin+(yMeasureDistance*j)),(int)(xMargin+measureWidth*k),(int)(yMargin+194+(yMeasureDistance*j)));
			
			//draw top staff
			for(int i=0;i<5;i++)
				g.drawLine((int)(xMargin),(int)(yMargin+(16*i)+(yMeasureDistance*j)),(int)(xMargin+measureWidth*measuresPerLine),(int)(yMargin+(16*i)+(yMeasureDistance*j)));
			
			//draw bottom staff
			for(int i=0;i<5;i++)
				g.drawLine((int)(xMargin),(int)(yMargin+130+(16*i)+(yMeasureDistance*j)),(int)(xMargin+measureWidth*measuresPerLine),(int)(yMargin+130+(16*i)+(yMeasureDistance*j)));
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
