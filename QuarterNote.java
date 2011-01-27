package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class QuarterNote extends DrawableNote{
	
	public QuarterNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.QUARTERNOTE;
		
		
	}

	@Override
	public void draw(Graphics g) {
		
		g.fillOval((int)(x-4),(int)( y-4), 8, 8);
				
		//draw the line attached to it
		if(note.getPitch()>=70 || (note.getPitch()<60 && note.getPitch()>=50))
			g.drawLine((int)(x-4), (int)(y), (int)(x-4), (int)(y+4+16));
		else
			g.drawLine((int)(x+4), (int)(y), (int)(x+4), (int)(y-4-16));
		
		//if off the staff,draw a line over it (if it lies on a bar)
		if(note.getPitch()<60)
		{
			if(((yPositionOfNote(note.getPitch())-34)%16==0))
				g.drawLine((int)(x-8), (int)(y), (int)(x+8), (int)(y));
		}
		if((yPositionOfNote(note.getPitch())%16==0))
			g.drawLine((int)(x-8), (int)(y), (int)(x+8), (int)(y));
		
	}

}
