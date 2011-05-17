package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.profile.ProfileManager;
import crescendo.base.profile.SongPreference;
import crescendo.base.song.Creator;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class ScoreDisplay extends JPanel implements ActionListener {
	private static final long serialVersionUID=1L;
	
	private SheetMusic module;
	private SongModel model;
	private List<Track> activeTracks;
	private List<Track> audioTracks;
	
	private JButton playAgain;
	private JButton newSong;
	
	public ScoreDisplay(SheetMusic module,ScoreCalculator calc,SongModel model,List<Track> activeTracks,List<Track> audioTracks) {
		this.module=module;
		this.model=model;
		this.activeTracks=activeTracks;
		this.audioTracks=audioTracks;
		
		// Build creator string; we need this twice
		String creator=null;
		for (Creator cr : model.getCreators())
		{
			if (creator==null)
			{
				creator="";
			}
			else
			{
				creator+=", ";
			}
			creator+=cr.getType()+": "+cr.getName();
		}
		
		// update the profile's stored scores
		List<Integer> scores=null;
		for (SongPreference pref : ProfileManager.getInstance().getActiveProfile().getSongPreferences()) {
			if (pref.getSongName().equals(model.getTitle()) && pref.getCreator().equals(creator)) {
				scores=pref.getScores();
				break;
			}
		}
		if (scores!=null) {
			scores.add(calc.getCurrentScore());
			Collections.sort(scores);
			Collections.reverse(scores);
		}
		
		// build UI
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.weightx=1;
		c.weighty=0;
		c.anchor=GridBagConstraints.NORTHWEST;
		JLabel titleLabel=new JLabel("Results for "+model.getTitle());
		JLabel creatorLabel=new JLabel(creator);
		JLabel scoreLabel=new JLabel(calc.getCurrentGrade());
		JLabel detailedScoreLabel=new JLabel(calc.getCurrentPercent()+"%, "+calc.getCurrentScore()+" points");
		this.playAgain=new JButton("Play this Song Again");
		this.newSong=new JButton("Play a New Song");
		
		c.gridwidth=GridBagConstraints.REMAINDER;
		titleLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		titleLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(titleLabel,c);
		
		creatorLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,14));
		creatorLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(creatorLabel,c);
		
		c.anchor=GridBagConstraints.CENTER;
		scoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,96));
		scoreLabel.setForeground(new Color(100,200,100));
		panel.add(scoreLabel,c);
		
		detailedScoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
		panel.add(detailedScoreLabel,c);
		
		c.gridwidth=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		panel.add(this.playAgain,c);
		c.gridwidth=GridBagConstraints.REMAINDER;
		panel.add(this.newSong,c);
		
		this.add(panel);
		
		this.playAgain.addActionListener(this);
		this.newSong.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.playAgain) {
			this.module.loadSong(this.model,this.activeTracks,this.audioTracks);
		}
		else if (e.getSource()==this.newSong) {
			this.module.showSongSelectionScreen();
		}
	}
}
