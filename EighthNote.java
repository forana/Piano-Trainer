package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class EighthNote extends DrawableNote{
	
	public EighthNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.EIGHTHNOTE;
		
		
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x-4), (int)(y-4), 8, 8);
				
		//draw the line attached to it, and the tail
		if(note.getPitch()>=70 || (note.getPitch()<60 && note.getPitch()>=50))
		{
			g.drawLine((int)(x-4), (int)(y), (int)(x-4), (int)(y+4+16));
			g.drawLine((int)(x-4), (int)(y+4+16),(int)(x-4+8), (int)(y+4+16-8));
		}
		else
		{
			g.drawLine((int)(x+4),(int)( y),(int)( x+4), (int)(y-4-16));
			g.drawLine((int)(x+4), (int)(y-4-16),(int)(x+4+8), (int)(y-4-16+8));
		}
		
		
		
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
