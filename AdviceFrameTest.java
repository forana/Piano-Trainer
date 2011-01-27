package crescendo.sheetmusic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AdviceFrameTest
{
	private AdviceFrame af;
	
	@Before
	public void setup()
	{
		this.af=new AdviceFrame(null,null);
		JFrame frame=new JFrame("AdviceFrame Test");
		frame.setSize(400,200);
		frame.add(this.af);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Test
	public void testDisplay()
	{
	}

	@Test
	public void testEarlyNotes()
	{
		/*for (int i=0; i<10; i++)
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
			af.handleProcessedNoteEvent(new ProcessedNoteEvent(new NoteEvent(null,null,System.currentTimeMillis()+100),new MidiEvent(60,100,null),false));
		}*/
	}
	
	@After
	public void confirm()
	{
		JOptionPane.showMessageDialog(null,"Press ok to proceed");
		af.setVisible(false);
	}
}
