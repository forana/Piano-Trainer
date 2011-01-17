package crescendo.base;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;

/**
 * Plays notes through the software synthesizer, as encapsulated in a NoteEvent object.
 * 
 * @author forana
 */
public class AudioPlayer implements NoteEventListener,FlowController
{
	public static Instrument[] instrumentList;
	
	static {
		try {
			Synthesizer tempSynth=MidiSystem.getSynthesizer();
			tempSynth.open();
			instrumentList=tempSynth.getDefaultSoundbank().getInstruments();
			tempSynth.close();
		}
		catch (MidiUnavailableException e)
		{
			instrumentList=new Instrument[0];
		}
	}
	/**
	 * Associates track to channel and note data.
	 */
	private Map<Track,AudioPlayerChannel> channelMap;
	
	/**
	 * The underlying synthesizer object.
	 */
	private Synthesizer synth;
	
	/**
	 * Signifies whether or not the audio is currently being suspended.
	 */
	private boolean suspended;
	
	/**
	 * Creates a new AudioPlayer, contructed around a specific song.
	 * 
	 * @param songModel The song to tailor the AudioPlayer to.
	 * @param activeTrack The index of the track being played by the user.
	 */
	public AudioPlayer(SongModel songModel,Track activeTrack)
	{
		// initialize relation map
		this.channelMap=new HashMap<Track,AudioPlayerChannel>();
		
		boolean retry=false;
		do
		{
			try
			{
				// get synthesizer object
				this.synth=MidiSystem.getSynthesizer();
				// open it so we can get, well, anything out of it
				this.synth.open();
				retry=false;
			}
			catch (MidiUnavailableException e)
			{
				String title="MIDI Unavailable";
				String message="A MIDI system could not be found. The program will now exit.";
				if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.Response.RETRY)
				{
					retry=true;
				}
				else
				{
					System.exit(1);
				}
			}
		} while (retry);
		
		// get channels and instruments
		MidiChannel[] channels=this.synth.getChannels();
		
		// match channels to AudioPlayerChannel objects, but don't add the active track
		int currentChannel=0;
		
		// start off assuming we arent't suspended
		this.suspended=false;
		
		for (Track track : songModel.getTracks())
		{
			// don't add the active track, or an empty track
			if (track!=activeTrack && track.getNotes().size()>0)
			{
				if (currentChannel<channels.length)
				{
					Instrument instrument;
					try
					{
						
						instrument=instrumentList[track.getVoice()];
					}
					catch (ArrayIndexOutOfBoundsException e)
					{
						instrument=instrumentList[0];
						System.err.println("No instrument found for '"+track.getName()+"' (index "+track.getVoice()+")");
					}
					
					MidiChannel channel=channels[currentChannel];
					channel.programChange(instrument.getPatch().getBank(),instrument.getPatch().getProgram());
					
					AudioPlayerChannel playerChannel=new AudioPlayerChannel(channel);
					
					// add it to the map
					this.channelMap.put(track,playerChannel);
					
					// don't forget to increment this... pretty sure someone did once
					currentChannel++;
				}
				else
				{
					System.err.println("Clipping track \""+track.getName()+"\"");
				}
			}
		}
	}
	
	/**
	 * The current delay between giving the MIDI subsystem a note and the time
	 * it gets played. This value will not likely change over time, but it is not
	 * guaranteed to remain constant.
	 * 
	 * @return The current delay between giving the MIDI subsystem a note and the time
	 * it gets played, in milliseconds.
	 */
	public double getLatency()
	{
		// the division converts microseconds to milliseconds
		return this.synth.getLatency()/1000;
	}
	
	/**
	 * Processes a note, passing it along to the proper channel with the proper action.
	 * 
	 * @param noteEvent The NoteEvent to be processed.
	 */
	public void handleNoteEvent(NoteEvent noteEvent)
	{
		// pull relevant information out
		Note note=noteEvent.getNote();
		NoteAction action=noteEvent.getAction();
		Track track=note.getTrack();
		
		// match the track to the proper channel object
		AudioPlayerChannel channel=this.channelMap.get(track);
		
		if (channel!=null)
		{
			// execute the proper action
			if (action==NoteAction.BEGIN)
			{
				channel.playNote(note);
			}
			else
			{
				channel.stopNote(note);
			}
		}
	}
	
	/**
	 * Pauses all currently playing notes, stopping them, but allowing them to be restarted with a resume() call.
	 */
	public void pause()
	{
		this.suspended=false;
		for (AudioPlayerChannel channel : this.channelMap.values())
		{
			channel.pauseNotes();
		}
	}
	
	/**
	 * Resume playing any paused or suspended notes.
	 */
	public void resume()
	{
		if (!this.suspended)
		{
			for (AudioPlayerChannel channel : this.channelMap.values())
			{
				channel.resumeNotes();
			}
		}
		this.suspended=false;
	}
	
	/**
	 * Suspend all notes.
	 */
	public void suspend()
	{
		// trip flag so we don't blast out new notes
		this.suspended=true;
	}
	
	/**
	 * Stop all currently playing notes, without allowing the notes to be resumed.
	 */
	public void stop()
	{
		this.suspended=false;
		for (AudioPlayerChannel channel : this.channelMap.values())
		{
			channel.stopAllNotes();
		}
	}
	
	/**
	 * Releases allocated synthesizer object. Once this method is called, all playing notes will stop, and subsequent
	 * calls to play notes will throw exceptions.
	 */
	public void songEnd()
	{
		// stop all playing notes
		this.stop();
		// close the synthesizer so it releases its resources
		this.synth.close();
	}
	
	/**
	 * Allows a collection of active notes to be paired with a MidiChannel object.
	 */
	private class AudioPlayerChannel
	{
		private MidiChannel channel;
		
		/**
		 * Contains a list of active notes and their current velocities.
		 * 
		 * @author forana
		 */
		private List<Note> activeNotes;
		
		/**
		 * Creates a new AudioPlayerChannel that wraps around a given MidiChannel.
		 * 
		 * @param channel The MidiChannel to play notes on.
		 */
		public AudioPlayerChannel(MidiChannel channel)
		{
			this.channel=channel;
			this.activeNotes=new LinkedList<Note>();
		}
		
		/**
		 * Play (turn on) a note.
		 * 
		 * @param note The note.
		 */
		public void playNote(Note note)
		{
			int pitch=note.getPitch();
			int velocity=note.getDynamic();
			this.channel.noteOn(pitch,velocity);
			
			for (Iterator<Note> iter=this.activeNotes.iterator(); iter.hasNext();)
			{
				Note iterNote=iter.next();
				if (note.getPitch() == iterNote.getPitch())
				{
					iter.remove();
				}
			}
			this.activeNotes.add(note);
		}
		
		/**
		 * Stop (turn off) a note.
		 * 
		 * @param note The note.
		 */
		public void stopNote(Note note)
		{
			int pitch=note.getPitch();
			this.channel.noteOff(pitch);
			
			this.activeNotes.remove(note);
		}
		
		/**
		 * Pause this channel's notes, turning them off until resumeNotes is called.
		 */
		public void pauseNotes()
		{
			// don't remove them from the list
			this.channel.allNotesOff();
		}
		
		/**
		 * Resumes any paused notes.
		 */
		public void resumeNotes()
		{
			for (Note note : this.activeNotes)
			{
				int pitch=note.getPitch();
				int velocity=note.getDynamic();
				
				this.channel.noteOn(pitch,velocity);
			}
		}
		
		/**
		 * Turn off any playing notes and clear out list of active notes.
		 */
		public void stopAllNotes()
		{
			this.channel.allNotesOff();
			this.activeNotes=new LinkedList<Note>();
		}
	}
}
