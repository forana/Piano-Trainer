package crescendo.sheetmusic;

import java.awt.Graphics;

public class Sharp extends DrawableModifier{

	private DrawableNote note;
	
	public Sharp(DrawableNote n){
		note = n;
		x = n.getX()+n.getWidth();
		y = n.getY();
	}

	@Override
	public void draw(Graphics g) {
		// draw note first and absorb its color
		note.draw(g);
		
		render(g,x,y);
	}
	
	public static void render(Graphics g,int x,int y) {
		g.drawLine(x+2,y-6,x+10,y-7);
		g.drawLine(x+2,y-2,x+10,y-3);
		g.drawLine(x+4,y-7,x+4,y);
		g.drawLine(x+8,y-8,x+8,y-1);
	}
}
