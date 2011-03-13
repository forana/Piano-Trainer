package crescendo.lesson;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import crescendo.base.AudioPlayer;
import crescendo.base.SongPlayer;
import crescendo.base.SongValidator;
import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;
import crescendo.sheetmusic.MusicEngine;

public class MusicPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID=1L;
	
	private Icon playIcon;
	private Icon stopIcon;
	private MusicEngine engine;
	private JButton actionButton;
	private JLabel scoreLabel;
	private SongPlayer player;
	private LessonGrader grader;

	public MusicPanel(MusicItem item) throws IOException {
		this.setBackground(Color.WHITE);
		Font font=new Font(Font.SERIF,Font.BOLD,14);
		JPanel panel=new JPanel();
		panel.setBorder(new LineBorder(new Color(0x99,0x99,0x99),1));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setLayout(new GridBagLayout());
		SongModel model=SongFactory.generateSongFromFile(item.getSource());
		this.engine=new MusicEngine(model,item.getTrack());
		this.add(this.engine);
		this.playIcon=new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/play.png"));
		this.stopIcon=new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/stop.png"));
		this.actionButton=new JButton(playIcon);
		this.scoreLabel=new JLabel("Grade");
		this.scoreLabel.setFont(font);
		JScrollPane music=new JScrollPane(this.engine,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		music.setPreferredSize(new Dimension(600,400));
		GridBagConstraints c=new GridBagConstraints();
		c.fill=GridBagConstraints.VERTICAL;
		c.weightx=1;
		c.anchor=GridBagConstraints.WEST;
		c.ipadx=5;
		c.ipady=5;
		panel.add(actionButton,c);
		c.anchor=GridBagConstraints.CENTER;
		JLabel titleLabel=new JLabel(model.getTitle());
		titleLabel.setFont(font);
		panel.add(titleLabel,c);
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.anchor=GridBagConstraints.EAST;
		panel.add(this.scoreLabel,c);
		c.weightx=0;
		panel.add(music,c);
		this.add(panel);
		
		this.grader=new LessonGrader();
		this.player=new SongPlayer(model);
		SongValidator validator=new SongValidator(model,model.getTracks().get(item.getTrack()),item.getHeuristics());
		this.player.attach(validator,100);
		AudioPlayer audio=new AudioPlayer(model,model.getTracks().get(item.getTrack()));
		this.player.attach(audio,(int)audio.getLatency());
		validator.attach(this.grader);
		
		this.actionButton.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (this.actionButton.getIcon()==this.playIcon) {
			this.engine.play();
			this.player.play();
			this.actionButton.setIcon(this.stopIcon);
		} else {
			this.engine.stop();
			this.player.stop();
			this.actionButton.setIcon(this.playIcon);
		}
	}
}