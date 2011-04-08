package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class QuarterRest extends DrawableNote{
	
	public QuarterRest() {}
	
	public QuarterRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.QUARTERNOTE;
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new QuarterRest(n,x,y);
	}
	
	public int getWidth() {
		return 12;
	}
	
	public double getBeatsCovered() {
		return 1;
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawLine((int)(x-2), (int)(y-16), (int)(x+5), (int)(y-8));
		g.drawLine((int)(x+5), (int)(y-8), (int)(x-2), (int)(y+8));
		g.drawLine((int)(x-2), (int)(y+8), (int)(x+5), (int)(y+16));
		g.drawArc((int)(x), (int)(y+16), 8, 12, 0, 270);
	}
}
