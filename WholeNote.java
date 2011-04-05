package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class WholeNote extends DrawableNote{
	
	public WholeNote() {}
	
	public WholeNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.WHOLENOTE;
		
		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new WholeRest(n,x,y);
	}
	
	public int getWidth() {
		return 8;
	}
	
	public double getBeatsCovered() {
		return 4;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		//draw the base circle
		g.drawOval(x,y,8,8);
	}
}
