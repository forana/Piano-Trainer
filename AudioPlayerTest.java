package crescendo.base;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import crescendo.base.song.SongModel;
import java.util.LinkedList;
import crescendo.base.song.Note;
import crescendo.base.song.Track;
import crescendo.base.AudioPlayer;
import crescendo.base.NoteEvent;

public class AudioPlayerTest
{
	private SongModel model;
	private Track track;
	
	@Before
	public void setupSong()
	{
		LinkedList<Track> trackList=new LinkedList<Track>();
		this.track=new Track("Track 1",57); // trumpet
		trackList.add(this.track);
		this.model=new SongModel(trackList,null,null,null,null,null,120,null);
	}
	
	@Test
	public void testSetup()
	{
		Assert.assertNotNull(this.model);
		Assert.assertNotNull(this.track);
	}
	
	@Test
	public void testPlaySingleNoteFiveTimesWhileStopping()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		for (int i=0; i<5; i++)
		{
			Note middleC=new Note(60,1000,100,this.track);
			NoteEvent startEvent=new NoteEvent(middleC,NoteAction.BEGIN,0);
			NoteEvent endEvent=new NoteEvent(middleC,NoteAction.END,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
			player.handleNoteEvent(endEvent);
		}
	}
	
	@Test
	public void testPlaySingleNoteFiveTimesWithoutStopping()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note c=new Note(73,1000,100,this.track);
		NoteEvent startEvent=new NoteEvent(c,NoteAction.BEGIN,0);
		NoteEvent endEvent=new NoteEvent(c,NoteAction.END,0);
		for (int i=0; i<5; i++)
		{
			
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		player.handleNoteEvent(endEvent);
	}
	
	@Test
	public void testMultipleSimultaneousNotes()
	{	
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note[] notes={new Note(58,1000,100,this.track),
		              new Note(60,1000,100,this.track),
		              new Note(62,1000,100,this.track),
		              new Note(63,1000,100,this.track),
		              new Note(65,1000,100,this.track)};
		for (int i=0; i<5; i++)
		{
			NoteEvent startEvent=new NoteEvent(notes[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		try
		{
			Thread.sleep(400);
		}
		catch (InterruptedException e)
		{
		}
		for (int i=4; i>=0; i--)
		{
			NoteEvent endEvent=new NoteEvent(notes[i],NoteAction.END,0);
			player.handleNoteEvent(endEvent);
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	@Test
	public void testPauseResume()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note[] notes={new Note(58,1000,100,this.track),
		              new Note(60,1000,100,this.track),
		              new Note(62,1000,100,this.track),
		              new Note(63,1000,100,this.track),
		              new Note(65,1000,100,this.track)};
		for (int i=0; i<5; i++)
		{
			NoteEvent startEvent=new NoteEvent(notes[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		try
		{
			Thread.sleep(400);
		}
		catch (InterruptedException e)
		{
		}
		player.pause();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		player.resume();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		for (int i=0; i<5; i++)
		{
			NoteEvent event=new NoteEvent(notes[i],NoteAction.END,0);
			player.handleNoteEvent(event);
		}
	}
	
	@Test
	public void testStop()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note[] notes={new Note(71,1000,100,this.track),
		              new Note(73,1000,100,this.track),
		              new Note(75,1000,100,this.track),
		              new Note(76,1000,100,this.track),
		              new Note(78,1000,100,this.track)};
		for (int i=0; i<5; i++)
		{
			NoteEvent startEvent=new NoteEvent(notes[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		try
		{
			Thread.sleep(400);
		}
		catch (InterruptedException e)
		{
		}
		player.stop();
	}
	
	@Test
	public void testSuspendResume()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note[] notes={new Note(58,1000,100,this.track),
		              new Note(60,1000,100,this.track),
		              new Note(62,1000,100,this.track),
		              new Note(63,1000,100,this.track),
		              new Note(65,1000,100,this.track)};
		for (int i=0; i<5; i++)
		{
			NoteEvent startEvent=new NoteEvent(notes[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		try
		{
			Thread.sleep(400);
		}
		catch (InterruptedException e)
		{
		}
		System.out.println("suspending");
		player.suspend();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		player.resume();
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		for (int i=0; i<5; i++)
		{
			NoteEvent event=new NoteEvent(notes[i],NoteAction.END,0);
			player.handleNoteEvent(event);
		}
	}
	
	@Test
	public void testMultipleSimultaneousTracks()
	{
		Track track1=new Track("Aah",53);
		Track track2=new Track("Tuba",59);
		LinkedList<Track> trackList=new LinkedList<Track>();
		trackList.add(track1);
		trackList.add(track2);
		SongModel twoModel=new SongModel(trackList,null,null,null,null,null,0,null);
		AudioPlayer player=new AudioPlayer(twoModel,null);
		Note[] notes1={new Note(73,1,100,track1),
		               new Note(60,1,100,track1),
		               new Note(73,1,100,track1),
		               new Note(60,1,100,track1),
		               new Note(73,1,100,track1),
		               new Note(60,1,100,track1),
		               new Note(73,1,100,track1),
		               new Note(60,1,100,track1)};
		Note[] notes2={new Note(34,1,100,track2),
		               new Note(36,1,100,track2),
		               new Note(38,1,100,track2),
		               new Note(39,1,100,track2),
		               new Note(41,1,100,track2),
		               new Note(39,1,100,track2),
		               new Note(38,1,100,track2),
		               new Note(36,1,100,track2)};
		for (int i=0; i<8; i++)
		{
			NoteEvent event1=new NoteEvent(notes1[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(event1);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
			NoteEvent event2=new NoteEvent(notes2[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(event2);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
			NoteEvent event3=new NoteEvent(notes1[i],NoteAction.END,0);
			player.handleNoteEvent(event3);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
			NoteEvent event4=new NoteEvent(notes2[i],NoteAction.END,0);
			player.handleNoteEvent(event4);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	@Test
	public void testDynamic()
	{
		AudioPlayer player=new AudioPlayer(this.model,null);
		Note[] notes={new Note(60,1000,20,this.track),
		              new Note(60,1000,40,this.track),
		              new Note(60,1000,60,this.track),
		              new Note(60,1000,80,this.track),
		              new Note(60,1000,100,this.track)};
		for (int i=0; i<5; i++)
		{
			NoteEvent startEvent=new NoteEvent(notes[i],NoteAction.BEGIN,0);
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		// since they all have the same pitch, any of them should end it
		NoteEvent endEvent=new NoteEvent(notes[0],NoteAction.END,0);
		player.handleNoteEvent(endEvent);
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	@Test
	public void testActiveTrack()
	{
		AudioPlayer player=new AudioPlayer(this.model,this.track);
		Note c=new Note(73,1000,100,this.track);
		NoteEvent startEvent=new NoteEvent(c,NoteAction.BEGIN,0);
		NoteEvent endEvent=new NoteEvent(c,NoteAction.END,0);
		for (int i=0; i<5; i++)
		{
			
			player.handleNoteEvent(startEvent);
			try
			{
				Thread.sleep(200);
			}
			catch (InterruptedException e)
			{
			}
		}
		player.handleNoteEvent(endEvent);
	}
	
	@After
	public void delay()
	{
		// hang for half a second
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException e)
		{
		}
	}
}
