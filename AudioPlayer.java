package crescendo.base;

import java.util.List;
import java.util.LinkedList;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Instrument;

public class AudioPlayer
{
	private MidiChannel[] channels;
	
	private List<Integer>[] activeNotes;
	
	private Synthesizer synth;
	
	public AudioPlayer(SongModel songModel)
	{
	}
}
