package crescendo.base.parsing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

public class MusicXmlParserTest {
	
	@Test
	public void TestMiddleC() throws IOException{
		SongFileParser mxl = new MusicXmlParser();
		SongModel model = mxl.parse(new File("resources/pokerface.mxl"));
		
		assertEquals(model.getTitle(),"Middle C");
		assertEquals(model.getLicense(),"GPL");
		for(int i=0;i<model.getCreators().size();i++){
			if(model.getCreators().get(i).getType().equals("composer")){
				assertEquals(model.getCreators().get(i).getName(),"Recordare");
			}else if(model.getCreators().get(i).getType().equals("editor")){
				assertEquals(model.getCreators().get(i).getName(),"Nick Gartmann");
			}else{
				fail("Creator of type "+model.getCreators().get(i).getType()+" was not expected for this song");
			}
		}
		
		assertEquals(model.getBPM(),90);
		assertTrue(model.getTimeSignature().getBeatNote()==4);
		assertTrue(model.getTimeSignature().getBeatsPerMeasure()==4);
		
		List<Track> tracks = model.getTracks();
		
		//test tracks
		assertEquals(tracks.size(),1);
		assertEquals(tracks.get(0).getName(),"Music");
		assertEquals(tracks.get(0).getVoice(),10);
		
		//test notes
		assertEquals(tracks.get(0).getNotes().size(),2);
		assertTrue(tracks.get(0).getNotes().get(0).getDuration()==4);
		assertEquals(tracks.get(0).getNotes().get(0).getPitch(),60);
		assertTrue(tracks.get(0).getNotes().get(1).getDuration()==4);
		assertEquals(tracks.get(0).getNotes().get(1).getDynamic(),0);
	}
}
