package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class MusicEngine extends JPanel implements ProcessedNoteEventListener,ComponentListener {
	private static final long serialVersionUID=1L;
	
	private static final int MARGIN=10;
	private static final int HEADER_HEIGHT=30;
	private static final int CLEF_WIDTH=24;
	private static final int STAFF_LINE_HEIGHT=8;
	private static final int STAFF_HEIGHT=4*STAFF_LINE_HEIGHT;
	private static final int STAFF_MARGIN=STAFF_HEIGHT;
	private static final int STAFF_SPACING=0;
	private static final int STAFF_FULL_HEIGHT=STAFF_MARGIN*2+STAFF_HEIGHT+STAFF_SPACING;
	
	private static final DrawableNote[] NOTE_PROTOTYPES=new DrawableNote[]{
		new WholeNote(),
		new HalfNote(),
		new QuarterNote(),
		new EighthNote(),
		new SixteenthNote()
	};
	
	private static final DrawableNote[] REST_PROTOTYPES=new DrawableNote[]{
		new WholeRest(),
		new HalfRest(),
		new QuarterRest(),
		new EighthRest(),
		new SixteenthRest()
	};
	
	//data items
	private SongModel model;
	private Track activeTrack;
	private double beatsPerMeasure;
	private double beatNote;
	private int measureCount;
	
	//alignment items
	private boolean built;
	private Map<Note,List<Drawable>> noteMap;
	private boolean titleShowing;
	private int measureWidth=150; // maybe someday this won't be hardcoded
	private int decoratorWidth=0;
	private int measuresPerLine;
	private double beatWidth;
	
	public MusicEngine(SongModel model,Track activeTrack){
		this(model,activeTrack,true);
	}

	public MusicEngine(SongModel model,Track activeTrack,boolean showTitle){
		this.model=model;
		this.activeTrack=activeTrack;
		this.titleShowing=showTitle;
		this.addComponentListener(this);
		
		this.beatsPerMeasure=model.getTimeSignature().getBeatsPerMeasure();
		this.beatNote=model.getTimeSignature().getBeatNote();
		this.measureCount=(int)Math.ceil(model.getDuration()/beatsPerMeasure);
		
		this.built=false;
	}
	
	private void build()
	{
		if (this.getWidth()>0) // if the width is 0... don't even try
		{
			// set some base values
			this.decoratorWidth=12; // guessing
			this.measuresPerLine=(int)Math.floor((this.getWidth()-2*MARGIN-CLEF_WIDTH-this.decoratorWidth)/this.measureWidth);
			int height=this.titleShowing?HEADER_HEIGHT:0;
			height+=2*MARGIN;
			height+=(STAFF_HEIGHT+2*STAFF_MARGIN+STAFF_SPACING)*(int)Math.ceil(1.0*this.measureCount/this.measuresPerLine);
			this.setPreferredSize(new Dimension(this.getWidth(),height));
			this.beatWidth=1/this.beatNote*this.measureWidth;
			
			// add notes to list
			this.noteMap=new HashMap<Note,List<Drawable>>();
			double beatOffset=0;
			double leftInMeasure=this.beatsPerMeasure;
			for (Note note : activeTrack.getNotes())
			{
				noteMap.put(note,calculateNotes(note,leftInMeasure,beatOffset));
				beatOffset+=note.getDuration();
				leftInMeasure+=note.getDuration();
				while (leftInMeasure>=this.beatsPerMeasure)
				{
					leftInMeasure-=this.beatsPerMeasure;
				}
			}
			
			// good to go
			this.built=true;
		}
		this.repaint();
	}
	
	private List<Drawable> calculateNotes(Note note,double beatsLeft,double beatOffset) {
		List<Drawable> ret=new LinkedList<Drawable>();
		double duration=note.getDuration();
		while (duration>beatsLeft)
		{
			ret.addAll(splitNote(note,beatsLeft,beatOffset));
			beatOffset+=beatsLeft;
			duration-=beatsLeft;
			beatsLeft=this.beatsPerMeasure;
		}
		if (duration>0)
		{
			ret.addAll(splitNote(note,duration,beatOffset));
		}
		return ret;
	}
	
	private List<Drawable> splitNote(Note note,double duration,double beatOffset) {
		List<Drawable> ret=new LinkedList<Drawable>();
		for (int i=0; i<NOTE_PROTOTYPES.length;)
		{
			DrawableNote[] prototypeArray=note.isPlayable()?NOTE_PROTOTYPES:REST_PROTOTYPES;
			if (prototypeArray[i].getBeatsCovered()>duration)
			{
				i++;
			}
			else
			{
				Drawable d=prototypeArray[i].spawn(note,
					getX(beatOffset,prototypeArray[i].getBeatsCovered(),prototypeArray[i].getWidth()),
					getY(note,beatOffset));
				if (isSharp(note.getPitch()))
				{
					d=new Sharp((DrawableNote)d);
				}
				ret.add(d);
				duration-=prototypeArray[i].getBeatsCovered();
				beatOffset+=prototypeArray[i].getBeatsCovered();
			}
		}
		return ret;
	}
	
	private int getX(double beats,double count,double width) {
		int xb=MARGIN+CLEF_WIDTH+this.decoratorWidth;
		while (beats>this.beatsPerMeasure*this.measuresPerLine)
		{
			beats-=this.beatsPerMeasure*this.measuresPerLine;
		}
		xb+=(int)(beats*this.beatWidth);
		xb+=(int)(count*this.beatWidth+width)/2;
		return xb;
	}
	
	private int getY(Note n,double beats) {
		int yb=MARGIN+STAFF_MARGIN+(this.titleShowing?HEADER_HEIGHT:0);
		if (n.isPlayable())
		{
			int pitch=n.getPitch();
			// middle C (60) is one full bar down
			yb+=STAFF_LINE_HEIGHT;
			int sign=(int)Math.signum(72-pitch);
			for (int i=pitch; i!=72; i+=sign)
			{
				if (!isSharp(i))
				{
					yb+=sign*STAFF_LINE_HEIGHT/2;
				}
			}
		}
		else // it's a rest, just give it the middle of the staff
		{
			yb+=STAFF_HEIGHT/2;
		}
		while (beats>this.beatsPerMeasure*this.measuresPerLine)
		{
			beats-=this.beatsPerMeasure*this.measuresPerLine;
			yb+=STAFF_FULL_HEIGHT;
		}
		return yb;
	}
	
	public void setSection(Note startNote,Note endNote) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void paint(Graphics g){
		if (this.getWidth()>0 && this.built)
		{
			// wipe it
			g.setColor(Color.WHITE);
			g.fillRect(0,0,this.getWidth(),this.getHeight());
			// draw staff lines
			g.setColor(Color.BLACK);
			int xb=MARGIN;
			int lead=CLEF_WIDTH+this.decoratorWidth;
			int yb=MARGIN+(this.titleShowing?HEADER_HEIGHT:0);
			for (int i=0; i<Math.ceil(1.0*this.measureCount/this.measuresPerLine); i++)
			{
				int measures=this.measuresPerLine;
				if (i==this.measureCount/this.measuresPerLine)
				{
					measures=this.measureCount%this.measuresPerLine;
				}
				int lw=lead+measures*this.measureWidth;
				yb+=STAFF_MARGIN;
				// draw clef
				g.drawLine(xb,yb,xb+CLEF_WIDTH,yb+STAFF_HEIGHT);
				g.drawLine(xb+CLEF_WIDTH,yb,xb,yb+STAFF_HEIGHT);
				// draw time signature
				g.setFont(new Font(Font.SERIF,Font.BOLD,STAFF_HEIGHT/2));
				g.drawString(Integer.toString((int)this.model.getTimeSignature().getBeatsPerMeasure()),xb+CLEF_WIDTH,yb+STAFF_HEIGHT/2-2);
				g.drawString(Integer.toString((int)this.model.getTimeSignature().getBeatNote()),xb+CLEF_WIDTH,yb+STAFF_HEIGHT-2);
				// draw vertical lines
				for (int j=0; j<measures; j++)
				{
					g.drawLine(xb+lead+(j+1)*this.measureWidth,yb,xb+lead+(j+1)*this.measureWidth,yb+STAFF_HEIGHT);
				}
				g.drawLine(xb,yb,xb,yb+STAFF_HEIGHT);
				// draw horizontal lines
				for (int j=0; j<5; j++)
				{
					if (j!=0)
					{
						yb+=STAFF_LINE_HEIGHT;
					}
					g.drawLine(xb,yb,xb+lw,yb);
				}
				yb+=STAFF_MARGIN+STAFF_SPACING;
				// draw notes
				for (Note note : this.activeTrack.getNotes())
				{
					for (Drawable draw : this.noteMap.get(note))
					{
						draw.draw(g);
					}
				}
			}
		}
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
	}
	
	private static boolean isSharp(int n)
	{
		return n%12==1 || n%12==3 || n%12==6 || n%12==8 || n%12==10;
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentShown(ComponentEvent arg0) {}
	
	public void componentResized(ComponentEvent arg0)
	{
		this.build();
		this.updateUI();
	}
}