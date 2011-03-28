package crescendo.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import crescendo.base.AudioPlayer;
import crescendo.base.ErrorHandler;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class TrackSelectionPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID=1L;

	private GameModule module;
	private SongModel model;
	private JCheckBox[] active;
	private JCheckBox[] enabled;
	private JComboBox[] instruments;
	
	private JButton play1;
	private JButton play2;
	
	public TrackSelectionPanel(GameModule module,SongModel model) {
		this.module=module;
		this.model=model;
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		JPanel form=new JPanel();
		form.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=0;
		c.weighty=0;
		c.ipadx=5;
		c.fill=GridBagConstraints.NONE;
		
		JLabel infoLabel=new JLabel("Song: "+model.getTitle());
		form.add(infoLabel,c);
		
		c.gridwidth=GridBagConstraints.RELATIVE;
		form.add(new JLabel("Select options below, then click Play."),c);
		
		c.gridwidth=GridBagConstraints.REMAINDER;
		play1=new JButton("Play");
		play1.addActionListener(this);
		form.add(play1,c);
		
		int size=model.getTracks().size();
		active=new JCheckBox[size];
		enabled=new JCheckBox[size];
		instruments=new JComboBox[size];
		String[] isn=new String[AudioPlayer.instrumentList.length];
		for (int i=0; i<isn.length && i<128; i++)
		{
			isn[i]=AudioPlayer.instrumentList[i].getName();
		}
		
		for (int i=0; i<size; i++)
		{
			Track track=model.getTracks().get(i);
			c.gridwidth=1;
			
			JLabel title=new JLabel(track.getName());
			form.add(title,c);
			
			active[i]=new JCheckBox("I will play this track");
			active[i].setSelected(i==0);
			active[i].addActionListener(this);
			form.add(active[i],c);
			
			enabled[i]=new JCheckBox("Include this track");
			enabled[i].setSelected(true);
			form.add(enabled[i],c);
			
			c.gridwidth=GridBagConstraints.REMAINDER;
			instruments[i]=new JComboBox(isn);
			instruments[i].setSelectedIndex(track.getVoice());
			form.add(instruments[i],c);
		}
		
		play2=new JButton("Play");
		play2.addActionListener(this);
		c.gridwidth=GridBagConstraints.REMAINDER;
		form.add(play2,c);
		
		this.add(new JScrollPane(form));
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==play1 || e.getSource()==play2)
		{
			// check if active track checkboxes haven't been somehow thwarted
			int activetrack=-1;
			for (int i=0; i<active.length; i++)
			{
				if (active[i].isSelected())
				{
					activetrack=i;
				}
			}
			
			if (activetrack<0)
			{
				ErrorHandler.showNotification("No Active Track","You must select a track to play yourself.");
			}
			else
			{
				// go track by track
				List<Track> newTracks=new LinkedList<Track>();
				Track activeRef=null;
				for (int i=0; i<model.getTracks().size(); i++)
				{
					Track track=model.getTracks().get(i);
					if (enabled[i].isSelected() || i==activetrack)
					{
						track.setVoice(instruments[i].getSelectedIndex());
						newTracks.add(track);
						
						if (i==activetrack)
						{
							activeRef=track;
						}
					}
				}
				
				SongModel newModel=new SongModel(newTracks,model.getTitle(),model.getCreators(),model.getLicense(),model.getBPM(),model.getTimeSignature(),model.getKeySignature());
				module.showGamePanel(newModel,activeRef);
			}
		}
		else // has to be one of the active checkboxes
		{
			for (int i=0; i<active.length; i++)
			{
				active[i].setSelected(e.getSource()==active[i]);
				if (!enabled[i].isSelected() && active[i].isSelected())
				{
					enabled[i].setSelected(true);
				}
			}
		}
	}
}
