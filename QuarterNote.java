package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class QuarterNote extends DrawableNote{
	
	public QuarterNote() {}
	
	public QuarterNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.QUARTERNOTE;
		
		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new QuarterNote(n,x,y);
	}
	
	public int getWidth() {
		return 8;
	}
	
	public double getBeatsCovered() {
		return 1;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		//draw the base circle
		g.fillOval(x,y,8,8);
		
		//draw the line attached to it
		if(note.getPitch()<79)
			g.drawLine(x+8,y+4,x+8,y-16);
		else
			g.drawLine(x,y+4,x,y+24);
	}
}
