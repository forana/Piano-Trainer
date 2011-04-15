package crescendo.sheetmusic;

import java.awt.Graphics;

public class Flat extends DrawableModifier{
	private DrawableNote note;
	
	public Flat(DrawableNote n){
		note = n;
		x = n.getX()+n.getWidth();
		y = n.getY();
	}

	@Override
	public void draw(Graphics g) {
		note.draw(g);
		render(g,x,y);
	}
	
	public static void render(Graphics g,int x,int y)
	{
		g.drawLine(x,y-MusicEngine.STAFF_LINE_HEIGHT,x,y+(int)(1.5*MusicEngine.STAFF_LINE_HEIGHT));
		g.drawArc(x-MusicEngine.STAFF_LINE_HEIGHT/2,y+MusicEngine.STAFF_LINE_HEIGHT/2,MusicEngine.STAFF_LINE_HEIGHT,MusicEngine.STAFF_LINE_HEIGHT,270,180);
	}
}
