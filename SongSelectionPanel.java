package crescendo.game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

import crescendo.base.ErrorHandler;
import crescendo.base.profile.ProfileManager;
import crescendo.base.profile.SongScore;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SongSelectionPanel extends JScrollPane implements ActionListener {
	private static final long serialVersionUID=1L;
	
	private JButton loadButton;
	private GameModule module;
	
	public SongSelectionPanel(GameModule module) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.module=module;
		this.loadButton=new JButton("Load new song");
		this.loadButton.addActionListener(this);
		
		JPanel superPanel=new JPanel();
		JPanel panel=new JPanel();
		
		//superPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=1;
		c.weighty=1;
		c.ipadx=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.anchor=GridBagConstraints.NORTHWEST;
		panel.add(this.loadButton,c);
		
		c.fill=GridBagConstraints.BOTH;
		
		for (final SongScore score : ProfileManager.getInstance().getActiveProfile().getGameScores())
		{
			c.gridwidth=GridBagConstraints.REMAINDER;
			panel.add(new JSeparator(JSeparator.HORIZONTAL),c);
			
			JPanel labelPanel=new JPanel();
			labelPanel.setLayout(new BoxLayout(labelPanel,BoxLayout.Y_AXIS));
			JLabel titleLabel=new JLabel(score.getTitle());
			titleLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
			labelPanel.add(titleLabel,c);
			JLabel authorLabel=new JLabel(score.getAuthor());
			authorLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
			labelPanel.add(authorLabel,c);
			c.gridwidth=1;
			panel.add(labelPanel,c);
			
			JLabel scoreLabel=new JLabel("High score: "+score.getHighScore());
			scoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,14));
			scoreLabel.setForeground(Color.BLUE);
			panel.add(scoreLabel,c);
			
			JButton playButton=new JButton("Play");
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try
					{
						SongModel model=SongFactory.generateSongFromFile(score.getFilePath());
						SongSelectionPanel.this.module.showTrackSelectionPanel(model);
					}
					catch (IOException ex)
					{
						ErrorHandler.showNotification("Error Loading File","Error loading \""+score.getFilePath()+"\":\n"+ex.getMessage());
					}
				}
			});
			c.gridwidth=GridBagConstraints.REMAINDER;
			panel.add(playButton,c);
		}
		
		// add extra row for fun and spacing
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.weightx=1;
		c.weighty=1;
		panel.add(new JPanel(),c);
		superPanel.add(panel);
		
		this.setViewportView(superPanel);
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc=new JFileChooser(ProfileManager.getInstance().getActiveProfile().getLastDirectory());
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Lesson Books (*.mid,*.midi,*.mxl,*.xml)","mid","midi","mxl","xml"));
		if (fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			File f=fc.getSelectedFile();
			ProfileManager.getInstance().getActiveProfile().setLastDirectory(f.getParentFile());
			String path=f.getAbsolutePath();
			try
			{
				SongModel model=SongFactory.generateSongFromFile(path);
				SongScore record=new SongScore(path,model);
				ProfileManager.getInstance().getActiveProfile().getGameScores().add(record);
				module.showTrackSelectionPanel(model);
			}
			catch (IOException ex)
			{
				ErrorHandler.showNotification("Error Loading Song","The file \""+f.getName()+"\" was in an unexpected format.\nError response: "+ex.getMessage());
			}
		}
	}
}
