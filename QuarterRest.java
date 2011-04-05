package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class QuarterRest extends DrawableNote{
	
	public QuarterRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.QUARTERNOTE;
		
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
		return new QuarterRest(n,x,y);
	}
	
	public int getWidth() {
		return 12;
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawLine((int)(x-2), (int)(y-16), (int)(x+5), (int)(y-8));
		g.drawLine((int)(x+5), (int)(y-8), (int)(x-2), (int)(y+8));
		g.drawLine((int)(x-2), (int)(y+8), (int)(x+5), (int)(y+16));
		g.drawArc((int)(x), (int)(y+16), 8, 12, 0, 270);
	}
}
