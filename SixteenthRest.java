package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class SixteenthRest extends DrawableNote{
	
	public SixteenthRest() {}
	
	public SixteenthRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.SIXTEENTHNOTE;
		
		if(this.note.getPitch()>=60)
		{
			this.y = y+yPositionOfNote(72);
		}
		else
		{
			this.y = y+yPositionOfNote(52);
		}
		
		
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new SixteenthRest(n,x,y);
	}
	
	public int getWidth() {
		return 10;
	}
	
	public double getBeatsCovered() {
		return 0.25;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		
		g.drawLine((int)(x+5), (int)(y-5), (int)(x-2), (int)(y+28));
		g.drawLine((int)(x+5), (int)(y-5), (int)(x-3), (int)(y+5));
		g.fillOval((int)(x-10), (int)(y-2),8, 8);
		
		g.drawLine((int)(x+5-2), (int)(y-5+8), (int)(x-3-2), (int)(y+5+8));
		g.fillOval((int)(x-10), (int)(y-2)+8,8, 8);
	}
}


