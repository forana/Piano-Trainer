package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class Flat extends DrawableModifier{
	private DrawableNote note;
	
	public Flat(DrawableNote n){
		note = n;
		x = n.getX();
		y = n.getY();
	}

	@Override
	public void draw(Graphics g) {
		note.draw(g);
		
		g.drawLine((int)(x-10), (int)(y-15), (int)(x-10), (int)(y+5));
		
		g.drawArc((int)(x-10-4), (int)(y+5-9), 8, 9, 270, 180);
		

		
	}
}
