package crescendo.base;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import crescendo.base.song.SongFactory;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class SongPlayerTest implements NoteEventListener{
	
	private final int expectedNotesAttach = 1;
	private final int expectedNotesDetach = 0;
	
	private SongModel testModel;
	private SongPlayer testPlayer;
	private List<NoteEvent> receivedNotes;
	
	
	public SongPlayerTest() throws IOException {
		testModel = SongFactory.generateSongFromFile("resources/morrowind.mid");
		testPlayer = new SongPlayer(testModel);
		AudioPlayer player = new AudioPlayer(testModel, new Track("Test Track",1));
		testPlayer.attach(player, 100);
		System.out.println("Starting");
		testPlayer.play();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//receivedNotes = new ArrayList<NoteEvent>();
	}
	
	@Test
	public void TestATest(){}
	/*
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
	*/
	@Override
	public void handleNoteEvent(NoteEvent e) {
		System.out.println(e.getAction());
	}
}
