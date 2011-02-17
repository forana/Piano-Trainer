package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class HalfRest extends DrawableNote{
	
	public HalfRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.HALFNOTE;
		
		if(this.note.getPitch()>=60)
		{
			this.y = y+yPositionOfNote(72);
		}
		else
		{
			this.y = y+yPositionOfNote(52);
		}
		
		
	}

	@Override
	public void draw(Graphics g) {
		
		g.fillRect((int)(x)+4, (int)(y)+4, 8, 4);
	}
}
