package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class HalfNote extends DrawableNote{
	
	public HalfNote() {}
	
	public HalfNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.HALFNOTE;
		
		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new HalfNote(n,x,y);
	}
	
	public int getWidth() {
		return 8;
	}
	
	public double getBeatsCovered() {
		return 2;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		//draw the base circle
		g.drawOval(x,y,8,8);
		
		//draw the line attached to it
		if(note.getPitch()>=70 || (note.getPitch()<60 && note.getPitch()>=50))
			g.drawLine(x+8,y+4,x+8,y-16);
		else
			g.drawLine(x,y+4,x,y+24);
		
		//if off the staff,draw a line over it (if it lies on a bar)
		if(note.getPitch()<60)
		{
			if(((yPositionOfNote(note.getPitch())-34)%16==0))
				g.drawLine(x-4,y,x+12,y);
		}
		//if((yPositionOfNote(note.getPitch())%16==0))
			//g.drawLine((int)(x-8), (int)(y), (int)(x+8), (int)(y));
	}
}
