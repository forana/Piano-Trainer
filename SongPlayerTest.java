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
	
	private final int expectedNotesAttach = -1;
	private final int expectedNotesDetach = 0;
	
	private SongModel testModel;
	private SongPlayer testPlayer;
	private List<NoteEvent> receivedNotes;
	
	
	public SongPlayerTest() throws IOException {
		testModel = SongFactory.generateSongFromFile("resources/middlec.mxl");
		testPlayer = new SongPlayer(testModel);
		receivedNotes = new ArrayList<NoteEvent>();
	}
	
	@After
	public void tearDown() {
		receivedNotes.clear();
	}
	
	@Test
	public void TestAttach() {
		testPlayer.attach(this, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if(receivedNotes.size()>expectedNotesAttach) {
			fail("Too many notes received: "+receivedNotes.size()+" - Expected: "+expectedNotesAttach);
		}else if(receivedNotes.size()<expectedNotesAttach) {
			fail("Too few notes received: "+receivedNotes.size()+" - Expected: "+expectedNotesAttach);
		}
	}
	
	public void TestDetach() {
		testPlayer.attach(this, 100);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if(receivedNotes.size()>expectedNotesDetach) {
			fail("Too many notes received: "+receivedNotes.size()+" - Expected: "+expectedNotesDetach);
		}
	}

	@Override
	public void handleNoteEvent(NoteEvent e) {
		receivedNotes.add(e);	
	}
}
