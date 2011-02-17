package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class Dot extends DrawableModifier{
	private Drawable note;
	
	public Dot(Drawable n){
		note = n;
	}

	@Override
	public void draw(Graphics g) {
		g.fillOval((int)note.getX()+10, (int)note.getY()-3, 4, 4);
		
	}
}
