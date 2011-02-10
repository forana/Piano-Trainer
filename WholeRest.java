package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class WholeRest extends DrawableNote{
	
	public WholeRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.WHOLENOTE;
		
		if(this.note.getPitch()>=60)
		{
			this.y = y+yPositionOfNote(73);
		}
		else
		{
			this.y = y+yPositionOfNote(53);
		}
		
		
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawRect((int)(x), (int)(y), 8, 4);
		g.drawString(note.getDuration()+"", (int)(x), (int)(y));
	}
}
