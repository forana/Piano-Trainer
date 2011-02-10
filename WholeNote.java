package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class WholeNote extends DrawableNote{
	
	public WholeNote(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.WHOLENOTE;
		
		
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		for(int i=0;i<5;i++)
			g.drawOval((int)(x-4+i), (int)(y-4), 8, 8);
		
		
		
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
