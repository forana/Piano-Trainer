package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class SixteenthNote extends DrawableNote{
	
	public SixteenthNote() {}
	
	public SixteenthNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.SIXTEENTHNOTE;		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new SixteenthNote(n,x,y);
	}
	
	public int getWidth() {
		return 8;
	}
	
	public double getBeatsCovered() {
		return 0.25;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		//draw the base circle
		g.fillOval(x,y,8,8);
		
		//draw the line attached to it, and the tail
		if(note.getPitch()>=70 || (note.getPitch()<60 && note.getPitch()>=50))
		{
			g.drawLine(x+8,y+4,x+8,y-16);
			g.drawLine(x+8,y-16,x+12,y-12);
			g.drawLine(x+8,y-13,x+12,y-9);
		}
		else
		{
			g.drawLine(x,y+4,x,y+24);
			g.drawLine(x,y+24,x-4,y+20);
			g.drawLine(x,y+21,x-4,y+17);
		}
	}

}
