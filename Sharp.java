package crescendo.sheetmusic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

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
		int q=(int)(MusicEngine.STAFF_LINE_HEIGHT*0.25);
		Graphics2D g2=(Graphics2D)g;
		Stroke stroke=g2.getStroke();
	
		// vertical lines
		g.drawLine(x+q,y-MusicEngine.STAFF_LINE_HEIGHT+q,x+q,y+2*MusicEngine.STAFF_LINE_HEIGHT);
		g.drawLine(x+q+MusicEngine.STAFF_LINE_HEIGHT/2,y-MusicEngine.STAFF_LINE_HEIGHT,x+q+MusicEngine.STAFF_LINE_HEIGHT/2,y+2*MusicEngine.STAFF_LINE_HEIGHT-q);
		// horizontal lines
		g2.setStroke(new BasicStroke(2));
		g.drawLine(x,y+q,x+MusicEngine.STAFF_LINE_HEIGHT,y-q);
		g.drawLine(x,y+MusicEngine.STAFF_LINE_HEIGHT+q,x+MusicEngine.STAFF_LINE_HEIGHT,y+MusicEngine.STAFF_LINE_HEIGHT-q);
		g2.setStroke(stroke);
	}
}
