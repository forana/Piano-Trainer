package crescendo.sheetmusic;

import javax.swing.JFrame;

public class FlowControllerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame e = new JFrame();
		e.setSize(800,600);
		
		
		DragMouseAdapter m = new DragMouseAdapter(800,600);
		//e.getContentPane().add(m);
		
		//FlowControllerBar f = new FlowControllerBar();
		//f.setSize(800,60);
		//f.setLocation(0,50);
		//e.add(f);
		SongSelectionScreen s = new SongSelectionScreen(800,600);
		e.getContentPane().add(s);
		e.setVisible(true);
		
	}

}
