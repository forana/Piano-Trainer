package crescendo.base.EventDispatcher;

//import org.junit.Assert;
import org.junit.Test;
//import org.junit.Before;
//import org.junit.After;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EventDispatcherTest implements InputEventListener,MidiEventListener
{
	private JTextArea textBox;
	private JScrollBar scrollBar;
	
	@Test
	public void testEventDispatcherSortOf() // ok really this will only fail if exceptions get thrown
	{
		// create frame
		JFrame frame=new JFrame();
		frame.setTitle("Event Test Frame");
		frame.setSize(640,480);
		frame.setLocationByPlatform(true);
		this.textBox=new JTextArea("Events will show up here.\n");
		this.textBox.setEditable(false);
		EventDispatcher.getInstance().registerComponent(this.textBox);
		JScrollPane scrollPane=new JScrollPane(this.textBox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollBar=scrollPane.getVerticalScrollBar();
		frame.add(scrollPane);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		this.textBox.grabFocus();
		JMenu midiDeviceMenu=new JMenu("Midi Devices");
		JMenu registerMenu=new JMenu("Register");
		final JMenuItem autoDetect=new JMenuItem("Autodetect");
		final JMenuItem unregister=new JMenuItem("Unregister");
		final JMenuItem register=new JMenuItem("Register");
		ActionListener listener=new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource()==autoDetect)
				{
					EventDispatcher.getInstance().detectMidiDevice(10);
				}
				else if (e.getSource()==unregister)
				{
					EventDispatcher.getInstance().unregisterComponent(textBox);
				}
				else if (e.getSource()==register)
				{
					EventDispatcher.getInstance().registerComponent(textBox);
				}
				else // it's one of the stupid ones
				{
					EventDispatcher.getInstance().setTransmitterDevice(
						EventDispatcher.getInstance().getTransmitterDevices().get(Integer.parseInt(e.getActionCommand())));
				}
			}
		};
		autoDetect.addActionListener(listener);
		for (int i=0; i<EventDispatcher.getInstance().getTransmitterDevices().size(); i++)
		{
			JMenuItem deviceItem=new JMenuItem(EventDispatcher.getInstance().getTransmitterDevices().get(i).getDeviceInfo().getName());
			deviceItem.setActionCommand(""+i);
			deviceItem.addActionListener(listener);
			midiDeviceMenu.add(deviceItem);
		}
		register.addActionListener(listener);
		unregister.addActionListener(listener);
		registerMenu.add(register);
		registerMenu.add(unregister);
		midiDeviceMenu.add(autoDetect);
		JMenuBar menuBar=new JMenuBar();
		menuBar.add(midiDeviceMenu);
		menuBar.add(registerMenu);
		frame.setJMenuBar(menuBar);
		// add things for this object in the event dispatcher
		EventDispatcher.getInstance().attach((InputEventListener)this);
		EventDispatcher.getInstance().attach((MidiEventListener)this);
		// wait around until frame is disposed
		while (true)
		{
			if (!frame.isDisplayable())
			{
				break;
			}
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}
	}
	
	public void handleInputEvent(InputEvent e)
	{
		this.textBox.append("InputEvent: "+e.getInputType()+" ");
		switch (e.getInputType())
		{
			case CLICK:
				this.textBox.append(e.getActionType()+" (button = "+((MouseEvent)e).getButton()+", x = "+((MouseEvent)e).getX()+", y = "+((MouseEvent)e).getY()+")");
				break;
			case KEYBOARD:
				this.textBox.append(e.getActionType()+" (key = "+((KeyboardEvent)e).getKey()+")");
				break;
			case MOUSEMOVE:
				this.textBox.append("I don't even");
				break;
			case SCROLL:
				this.textBox.append("I don't even");
				break;
			default:
				this.textBox.append("unrecognized event type (wut)");
				break;
		}
		this.textBox.append(" @ "+e.getTimestamp()+"\n");
		this.scrollBar.setValue(this.scrollBar.getMaximum());
	}
	
	public void handleMidiEvent(MidiEvent e)
	{
		this.textBox.append("MidiEvent: "+e.getAction()+" (note = "+e.getNote()+", velocity = "+e.getVelocity()+") @ "+e.getTimestamp()+"\n");
		this.scrollBar.setValue(this.scrollBar.getMaximum());
	}
}
