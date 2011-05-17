package crescendo.base;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;

public class SongPlayerTest implements NoteEventListener{
	
	private final int expectedNotesAttach = 1;
	private final int expectedNotesDetach = 0;
	private final int expectedNotesPause = 1;
	private final int expectedNotesResume = 2;
	private final int expectedNotesStop = 4;
	
	private static SongModel testModel;
	private SongPlayer testPlayer;
	private List<NoteEvent> receivedOnNotes;
	private List<NoteEvent> receivedOffNotes;
	
	static
	{
		try {
			testModel = SongFactory.generateSongFromFile("resources/songplayer_test.xml");
		} catch (IOException e) {}
	}
	
	public SongPlayerTest() throws IOException {

		testPlayer = new SongPlayer(testModel);
		receivedOnNotes = new ArrayList<NoteEvent>();
		receivedOffNotes = new ArrayList<NoteEvent>();		
	}
	
	@After
	public void tearDown() {
		receivedOnNotes.clear();
		receivedOffNotes.clear();
		testPlayer.stop();
	}
	
	@Test
	public void TestAttach() {
		testPlayer.attach(this, 10);
		testPlayer.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if(receivedOnNotes.size()>expectedNotesAttach) {
			fail("Too many notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesAttach);
		}else if(receivedOnNotes.size()<expectedNotesAttach) {
			fail("Too few notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesAttach);
		}
	}
	
	
	@Test
	public void TestDetach() {
		testPlayer.attach(this,10);
		testPlayer.detach(this);
		testPlayer.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if(receivedOnNotes.size()>expectedNotesDetach) {
			fail("Too many notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesDetach);
		}
	}

	@Test
	public void TestStop(){
		testPlayer.attach(this,10);
		testPlayer.play();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){}
		testPlayer.stop();
		testPlayer.play();
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){}
		if(receivedOnNotes.size()>expectedNotesStop) {
			fail("Too many notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesStop);
		}else if(receivedOnNotes.size()<expectedNotesStop) {
			fail("Too few notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesStop);
		}
	}
	
	
	@Test
	public void TestPause(){
		testPlayer.attach(this,10);
		testPlayer.play();
		try{
			Thread.sleep(300);
		}catch(InterruptedException e){}
		testPlayer.pause();
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){}
		if(receivedOnNotes.size()>expectedNotesPause) {
			fail("Too many notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesPause);
		}else if(receivedOnNotes.size()<expectedNotesPause) {
			fail("Too few notes received: "+receivedOnNotes.size()+" - Expected: "+expectedNotesPause);
		}
	}
	
	@Test
	public void TestResumeAbsolute(){
		testPlayer.attach(this,10);
		
		//Do we actually get the notes?
		testPlayer.play();
		try{
			Thread.sleep(300);
		}catch(InterruptedException e){}
		testPlayer.pause();
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){}
		testPlayer.resume();
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){}
		if(receivedOnNotes.size()>expectedNotesResume) {
			fail("Too many notes received: "+receivedOffNotes.size()+" - Expected: "+expectedNotesResume);
		}else if(receivedOnNotes.size()<expectedNotesResume) {
			fail("Too few notes received: "+receivedOffNotes.size()+" - Expected: "+expectedNotesResume);
		}
		
	}
		
	@Test
	public void TestResumeTiming(){
		int lookaheadOffset = 10;
		testPlayer.attach(this,lookaheadOffset);
		testPlayer.play();
		long now = System.currentTimeMillis();
		try{
			Thread.sleep(300);
		}catch(InterruptedException e){}
		testPlayer.pause();
		try{
			Thread.sleep(300);
		}catch(InterruptedException e){}
		testPlayer.resume();
		try{
			Thread.sleep(300);
		}catch(InterruptedException e){}
		
		if(now+800 - (receivedOffNotes.get(0).getTimestamp()-lookaheadOffset) > 10 ||
			now+800 - (receivedOffNotes.get(0).getTimestamp()-lookaheadOffset) < -10 ) {
			fail("Received end of note 1 outside of acceptable range: "+
					(now+800 - (receivedOffNotes.get(0).getTimestamp()-lookaheadOffset)));
		}
	}
	
	/*
	static
	{
		try {
			testModel = SongFactory.generateSongFromFile("resources/songplayer_test_chord.xml");
		} catch (IOException e) {}
	}
	*/
	
	@Test
	public void TestChord(){
		
	}
	
	@Test
	public void TestTie(){
		
	}
	@Override
	public void handleNoteEvent(NoteEvent e) {
		if(e.getAction()==NoteAction.BEGIN){
			receivedOnNotes.add(e);
		}else{
			receivedOffNotes.add(e);
		}
	}
}
