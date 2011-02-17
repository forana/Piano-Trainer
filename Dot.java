package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class Dot extends DrawableModifier{
	private DrawableNote dNote;
	
	public Dot(DrawableNote n){
		x =n.getX();
		y =n.getY();
		dNote = n;

	}

	@Override
	public void draw(Graphics g) {
		dNote.draw(g);
		g.fillOval((int)x+10, (int)y-2, 5, 5);
		
	}
}
