package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import crescendo.base.FlowController;
import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.song.Creator;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class MusicEngine extends JPanel implements ProcessedNoteEventListener,ComponentListener,Scrollable,FlowController,NoteEventListener {
	private static final long serialVersionUID=1L;
	
	private static final int MARGIN=10;
	private static final int HEADER_HEIGHT=52;
	public static final int CLEF_WIDTH=24;
	public static final int STAFF_LINE_HEIGHT=8;
	public static final int STAFF_HEIGHT=4*STAFF_LINE_HEIGHT;
	private static final int STAFF_MARGIN=STAFF_HEIGHT;
	private static final int STAFF_SPACING=0;
	private static final int STAFF_FULL_HEIGHT=STAFF_MARGIN*2+STAFF_HEIGHT+STAFF_SPACING;
	
	private static final String CLEF_TREBLE_PATH="resources/images/clef.treble.28x64.png";
	private static final String CLEF_BASS_PATH="resources/images/clef.bass.28x64";
	
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
	
	// images (want to make sure we only load these once for the entire program);
	private static Image TREBLE_CLEF=null;
	private static Image BASS_CLEF=null;
	
	private static void loadImages()
	{
		Toolkit tk=Toolkit.getDefaultToolkit();
		TREBLE_CLEF=tk.createImage(CLEF_TREBLE_PATH);
		BASS_CLEF=tk.createImage(CLEF_BASS_PATH);
	}
	
	//data items
	private SongModel model;
	private Track activeTrack;
	private double beatsPerMeasure;
	private double beatNote;
	private int measureCount;
	
	//alignment items
	private boolean built;
	private Map<Note,List<DrawableNote>> noteMap;
	private List<Drawable> drawables;
	private boolean titleShowing;
	private int measureWidth=200; // maybe someday this won't be hardcoded
	private int decoratorWidth=0;
	private int measuresPerLine;
	private double beatWidth;
	private int barX;
	private int barY;
	
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
		
		this.barX=-1;
		this.barY=-1;
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
			this.noteMap=new HashMap<Note,List<DrawableNote>>();
			this.drawables=new LinkedList<Drawable>();
			double beatOffset=0;
			double leftInMeasure=this.beatsPerMeasure;
			for (Note note : activeTrack.getNotes())
			{
				noteMap.put(note,calculateNotes(note,leftInMeasure,beatOffset));
				beatOffset+=note.getDuration();
				leftInMeasure-=note.getDuration();
				while (leftInMeasure<0)
				{
					leftInMeasure+=this.beatsPerMeasure;
				}
			}
			
			// good to go
			this.built=true;
		}
		this.repaint();
	}
	
	private List<DrawableNote> calculateNotes(Note note,double beatsLeft,double beatOffset) {
		List<DrawableNote> ret=new LinkedList<DrawableNote>();
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
		
		// add created notes to list of drawables, with modifiers added
		Drawable last=null;
		for (DrawableNote dn : ret)
		{
			Drawable d=dn;
			if (isSharp(note.getPitch()))
			{
				d=new Sharp((DrawableNote)d);
			}
			if (last!=null)
			{
				if (note.isPlayable())
				{
					last=new Tie(last,d);
				}
				this.drawables.add(last);
			}
			last=d;
		}
		if (last!=null)
		{
			this.drawables.add(last);
		}
		return ret;
	}
	
	private List<DrawableNote> splitNote(Note note,double duration,double beatOffset) {
		List<DrawableNote> ret=new LinkedList<DrawableNote>();
		for (int i=0; i<NOTE_PROTOTYPES.length;)
		{
			DrawableNote[] prototypeArray=note.isPlayable()?NOTE_PROTOTYPES:REST_PROTOTYPES;
			if (prototypeArray[i].getBeatsCovered()>duration)
			{
				i++;
			}
			else
			{
				DrawableNote spawned=prototypeArray[i].spawn(note,
					getX(beatOffset,prototypeArray[i].getBeatsCovered(),prototypeArray[i].getWidth()),
					getY(note,beatOffset));
				// add dot if it comes out EXACTLY right, otherwise assume ties will be used
				if (spawned.getBeatsCovered()*1.5==duration)
				{
					spawned=new DottedNote(spawned);
				}
				ret.add(spawned);
				duration-=spawned.getBeatsCovered();
				beatOffset+=spawned.getBeatsCovered();
			}
		}
		return ret;
	}
	
	private int getX(double beats,double count,double width) {
		// place on proper line
		int xb=MARGIN+CLEF_WIDTH+this.decoratorWidth;
		while (beats>=this.beatsPerMeasure*this.measuresPerLine)
		{
			beats-=this.beatsPerMeasure*this.measuresPerLine;
		}
		// place in proper measure
		xb+=(int)(beats*this.beatWidth);
		// center note in its alotted area
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
		while (beats>=this.beatsPerMeasure*this.measuresPerLine)
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
		synchronized(MusicEngine.class)
		{
			if (this.getWidth()>0 && this.built)
			{
				// wipe it
				g.setColor(Color.WHITE);
				g.fillRect(0,0,this.getWidth(),this.getHeight());
				// draw header
				g.setColor(Color.BLACK);
				if (this.titleShowing)
				{
					Font headerFont=new Font(Font.SERIF,Font.BOLD,24);
					g.setFont(headerFont);
					String title=this.model.getTitle();
					int width=this.getFontMetrics(headerFont).stringWidth(title);
					String creator=null;
					for (Creator c : this.model.getCreators())
					{
						if (creator==null)
						{
							creator="";
						}
						else
						{
							creator+=", ";
						}
						creator+=c.getType()+": "+c.getName();
					}
					g.drawString(title,(this.getWidth()-width)/2,24);
					headerFont=headerFont.deriveFont(Font.PLAIN,16);
					g.setFont(headerFont);
					width=this.getFontMetrics(headerFont).stringWidth(creator);
					g.drawString(creator,(this.getWidth()-width)/2,40);
				}
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
					if (TREBLE_CLEF==null)
					{
						loadImages();
					}
					g.drawImage(TREBLE_CLEF,MARGIN,yb-2*STAFF_LINE_HEIGHT,CLEF_WIDTH,2*STAFF_HEIGHT,this);
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
				}
				// draw notes
				for (Drawable draw : this.drawables)
				{
					draw.draw(g);
				}
				// draw line
				if (this.barX>=0)
				{
					g.setColor(Color.RED);
					g.drawLine(this.barX,this.barY,this.barX,this.barY+STAFF_HEIGHT+2*STAFF_MARGIN);
				}
			}
		}
	}

	public void play(){
		for (List<DrawableNote> noteList : this.noteMap.values())
		{
			for (DrawableNote note : noteList)
			{
				note.reset();
			}
		}
		this.barX=MARGIN+CLEF_WIDTH+this.decoratorWidth;
		this.barY=MARGIN+(this.titleShowing?HEADER_HEIGHT:0);
	}

	public void pause(){
	}

	public void stop(){
	}

	public void resume(){
	}
	
	public void suspend(){}
	public void songEnd(){}
	
	public void handleNoteEvent(NoteEvent e) {
		if (e.getNote().getTrack()==this.activeTrack && e.getAction()==NoteAction.BEGIN)
		{
			List<DrawableNote> noteList=this.noteMap.get(e.getNote());
			if (noteList!=null)
			{
				DrawableNote note=noteList.get(0);
				this.barX=note.getX()+note.getWidth()/2;
				this.barY=MARGIN+(this.titleShowing?HEADER_HEIGHT:0)+((note.getY()-MARGIN-(this.titleShowing?HEADER_HEIGHT:0))/STAFF_FULL_HEIGHT*STAFF_FULL_HEIGHT);
				this.repaint();
			}
		}
	}

	@Override
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		if (e.getExpectedNote().getAction()==NoteAction.BEGIN && e.getExpectedNote().getNote()!=null && this.noteMap.keySet().contains(e.getExpectedNote().getNote()))
		{
			for (DrawableNote note : this.noteMap.get(e.getExpectedNote().getNote()))
			{
				note.setCorrect(e.isCorrect());
				this.repaint();
			}
		}
	}
	
	public static boolean isSharp(int n)
	{
		return n%12==1 || n%12==3 || n%12==6 || n%12==8 || n%12==10;
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentShown(ComponentEvent arg0) {}
	
	public void componentResized(ComponentEvent arg0)
	{
		synchronized (MusicEngine.class)
		{
			this.build();
			this.updateUI();
		}
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 1;
	}
}