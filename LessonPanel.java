package crescendo.lesson;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public class LessonPanel extends JPanel implements ComponentListener,Scrollable {
	private static final long serialVersionUID = 1L;

	public LessonPanel(Lesson lesson,JComponent module) {
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBackground(Color.WHITE);
		JPanel header=new JPanel();
		header.setLayout(new FlowLayout(FlowLayout.CENTER));
		header.setBackground(Color.WHITE);
		JLabel titleLabel=new JLabel(lesson.getTitle());
		titleLabel.setFont(new Font(Font.SERIF,Font.BOLD,20));
		header.add(titleLabel);
		this.add(header);
		for (PageItem item : lesson.getItems())
		{
			this.add(item.getPanel(module));
		}
		this.addComponentListener(this);
	}
	
	public void componentResized(ComponentEvent e)
	{
		for (Component child : this.getComponents())
		{
			child.setMaximumSize(new Dimension(this.getWidth(),Integer.MAX_VALUE));
		}
	}
	
	public void componentShown(ComponentEvent e)
	{
		componentResized(e);
	}

	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return 10;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return 10;
	}
}
