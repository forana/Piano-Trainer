package crescendo.sheetmusic;

import java.awt.Graphics;
import java.util.List;

import crescendo.base.song.Note;

public class Sharp extends DrawableModifier{

	private DrawableNote note;
	
	public Sharp(DrawableNote n){
		note = n;
		x = n.getX();
		y = n.getY();
	}

	@Override
	public void draw(Graphics g) {
		note.draw(g);
		
		g.drawLine((int)(x+5), (int)(y+3), (int)(x+15), (int)(y-7));
		g.drawLine((int)(x+5), (int)(y+8), (int)(x+15), (int)(y-2));
		
		
		g.drawLine((int)(x+12), (int)(y-10), (int)(x+12), (int)(y+5));
		g.drawLine((int)(x+8), (int)(y-5), (int)(x+8), (int)(y+10));
		
	}
}
