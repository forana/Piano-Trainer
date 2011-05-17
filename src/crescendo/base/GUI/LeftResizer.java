package crescendo.base.GUI;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;
import javax.swing.JSeparator;

public class LeftResizer extends JSeparator
{
	private int x=-1;
	private JComponent component;
	private JComponent parent;
	
	public LeftResizer(JComponent c,JComponent p)
	{
		super(JSeparator.VERTICAL);
		this.component=c;
		this.parent=p;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON1) {
					x=e.getX();
				}
			}});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				x=e.getX();
				Dimension oldDim=component.getMaximumSize();
				Dimension newDim=new Dimension(Math.max(oldDim.width+x,0),oldDim.height);
				component.setPreferredSize(newDim);
				component.setMaximumSize(newDim);
				parent.updateUI();
			}});
	}
}
