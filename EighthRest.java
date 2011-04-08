package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class EighthRest extends DrawableNote{
	
	public EighthRest() {}
	
	public EighthRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.EIGHTHNOTE;
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new EighthRest(n,x,y);
	}
	
	public int getWidth() {
		return 17;
	}
	
	public double getBeatsCovered() {
		return 0.5;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(x-5,y+10,x+5,y-10);
		g.drawLine(x+5,y-10,x-5,y-6);
		g.fillOval(x-7,y-10,5,5);
	}
}
