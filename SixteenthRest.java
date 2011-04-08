package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class SixteenthRest extends DrawableNote{
	
	public SixteenthRest() {}
	
	public SixteenthRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.SIXTEENTHNOTE;
		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new SixteenthRest(n,x,y);
	}
	
	public double getBeatsCovered() {
		return 0.25;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(x-5,y+10,x+5,y-10);
		g.drawLine(x+5,y-10,x-5,y-6);
		g.fillOval(x-7,y-10,5,5);
		
		g.drawLine(x+3,y-6,x-3,y-2);
		g.fillOval(x-9,y-6,5,5);
	}
}


