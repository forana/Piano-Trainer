package crescendo.sheetmusic;

import crescendo.base.NoteAction;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.ProcessedNoteEvent;
import crescendo.base.SongState;
import crescendo.base.HeuristicsModel;
import crescendo.base.EventDispatcher.ActionType;

import java.util.List;
import java.util.LinkedList;

public class ScoreCalculator implements ProcessedNoteEventListener
{
	private static final int STREAK_STEP = 10;
	private static final int STREAK_MAX = 5;
	private static final double STREAK_MULTIPLIER = 0.2;
	
	private static final int SCORE_DYNAMIC_PERFECT=20;
	private static final int SCORE_PITCH_PERFECT=90;
	private static final int SCORE_TIMING_PERFECT=90;
	private static final int SCORE_DYNAMIC_MAX_DISTANCE=40;
	private static final int SCORE_PITCH_MAX_DISTANCE=3;
	
	private static final double SCORE_RELEASE_MOD=0.5;
	
	private static List<Grade> GRADES=null;
	private static final Grade A = new Grade("A",85);
	private static final Grade B = new Grade("B",70);
	private static final Grade C = new Grade("C",55);
	private static final Grade D = new Grade("D",40);
	private static final Grade F = new Grade("F",0);
	
	static {
		GRADES=new LinkedList<Grade>();
		GRADES.add(A);
		GRADES.add(B);
		GRADES.add(C);
		GRADES.add(D);
		GRADES.add(F);
	}
	
	private boolean listeningPitch;
	private boolean listeningDynamic;
	private SongState songState;
	private HeuristicsModel model;
	private int perfectTotal;
	private int actualTotal;
	private int streak;
	
	public ScoreCalculator(boolean listeningPitch,boolean listeningDynamic,SongState songState,HeuristicsModel model)
	{
		this.listeningPitch=listeningPitch;
		this.listeningDynamic=listeningDynamic;
		this.songState=songState;
		this.model=model;
		this.perfectTotal=0;
		this.actualTotal=0;
	}
	
	public void reset()
	{
		this.perfectTotal=0;
		this.actualTotal=0;
	}
	
	public int getCurrentScore()
	{
		return this.actualTotal;
	}
	
	public double getCurrentPercent()
	{
		if (this.perfectTotal==0)
		{
			return 0;
		}
		else
		{
			return (1000*actualTotal/perfectTotal)/10.0;
		}
	}
	
	public String getCurrentGrade()
	{
		String name="-";
		double p=this.getCurrentPercent();
		for (Grade grade : GRADES)
		{
			if (grade.getLowest()<=p)
			{
				name=grade.getName();
				break;
			}
		}
		return name;
	}
	
	public int getStreak()
	{
		return this.streak;
	}
	
	public void handleProcessedNoteEvent(ProcessedNoteEvent e)
	{
		int perfectChange=0;
		if (e.isCorrect())
		{
			this.streak++;
		}
		else if((e.getExpectedNote()!=null && e.getExpectedNote().getAction()==NoteAction.BEGIN) || (e.getExpectedNote()==null && e.getPlayedNote().getAction()==ActionType.PRESS))
		{
			this.streak=0;
		}
		if (e.getExpectedNote()!=null)
		{
			int actual=0;
			if (this.listeningDynamic)
			{
				perfectChange+=SCORE_DYNAMIC_PERFECT;
				if (e.getPlayedNote()!=null)
				{
					int dynamicScore=(int)Math.round(1.0*(SCORE_DYNAMIC_MAX_DISTANCE
						-Math.min(Math.abs(e.getExpectedNote().getNote().getDynamic()-e.getPlayedNote().getVelocity()),
							SCORE_DYNAMIC_MAX_DISTANCE))
						/SCORE_DYNAMIC_MAX_DISTANCE
						* SCORE_DYNAMIC_PERFECT);
					actual+=dynamicScore;
				}
			}
			if (this.listeningPitch)
			{
				perfectChange+=SCORE_PITCH_PERFECT;
				if (e.getPlayedNote()!=null)
				{
					int pitchScore=(int)Math.round(1.0*(SCORE_PITCH_MAX_DISTANCE
						-Math.min(Math.abs(e.getExpectedNote().getNote().getPitch()-e.getPlayedNote().getNote()),
							SCORE_PITCH_MAX_DISTANCE))
						/SCORE_PITCH_MAX_DISTANCE
						* SCORE_PITCH_PERFECT);
					actual+=pitchScore;
				}
			}
			perfectChange+=SCORE_TIMING_PERFECT;
			int interval=(int)(this.model.getTimingInterval()/(1.0*this.songState.getBPM()/60/1000));
			if (e.getPlayedNote()!=null)
			{
				int rhythmScore=(int)Math.round(1.0*(interval
					-Math.min(Math.abs(e.getExpectedNote().getTimestamp()-e.getPlayedNote().getTimestamp()),
						interval))
					/interval
					* SCORE_TIMING_PERFECT);
				actual+=rhythmScore;
			}
			
			int streakMod=Math.min(Math.max(1,this.streak/STREAK_STEP),STREAK_MAX);
			actual*=1+STREAK_MULTIPLIER*streakMod;
			perfectChange*=1+STREAK_MULTIPLIER;
			
			if (e.getExpectedNote().getAction()==NoteAction.END)
			{
				actual*=SCORE_RELEASE_MOD;
				perfectChange*=SCORE_RELEASE_MOD;
			}
			
			this.actualTotal+=actual;
			this.perfectTotal+=perfectChange;
		}
	}
	
	private static class Grade
	{
		private String name;
		private double lowerBound;
		
		public Grade(String name,double lowerBound)
		{
			this.name=name;
			this.lowerBound=lowerBound;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public double getLowest()
		{
			return this.lowerBound;
		}
	}
}
