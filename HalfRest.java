package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class HalfRest extends DrawableNote{
	
	public HalfRest() {}
	
	public HalfRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.HALFNOTE;
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new HalfRest(n,x,y);
	}
	
	public int getWidth() {
		return 8;
	}
	
	public double getBeatsCovered() {
		return 2;
	}

	@Override
	public void draw(Graphics g) {
		g.fillRect(x,y-4,8,4);
	}
}
