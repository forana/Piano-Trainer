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
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;

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
	private static final boolean DEBUG = false;
	
	// Channel 10 (9 if zero-indexed) is always reserved for percussion
	public static final int PERCUSSION_INDEX = 9;
	public static Instrument[] instrumentList;
	private static Synthesizer synth=null;
	
	static {
		boolean retry=true;
		boolean success=false;
		
		while (!success && retry)
		{
			try {
				synth=MidiSystem.getSynthesizer();
				synth.open();
				Soundbank bank=synth.getDefaultSoundbank();
				if (bank==null)
				{
					bank=MidiSystem.getSoundbank(new File("resources/soundbank/soundbank-min.gm"));
					synth.loadAllInstruments(bank);
				}
				instrumentList=bank.getInstruments();
				success=true;
			}
			catch (IOException e)
			{
				success=false;
			}
			catch (MidiUnavailableException e)
			{
				success=false;
			}
			catch (InvalidMidiDataException e)
			{
				success=false;
			}
			
			if (!success)
			{
				String title="MIDI Unavailable";
				String message="A MIDI system could not be found. Press retry to check again, or fail to proceed.\n"
					+ "If the program proceeds without a midi system, you will not be able to hear accompaniment.";
				while (retry)
				{
					if (ErrorHandler.showRetryFail(title,message)==ErrorHandler.Response.RETRY)
					{
						retry=true;
					}
					else
					{
						retry=false;
						synth=null;
					}
				}
			}
		}
	}
	/**
	 * Associates track to channel and note data.
	 */
	private Map<Track,AudioPlayerChannel> channelMap;
	
	/**
	 * Signifies whether or not the audio is currently being suspended.
	 */
	private boolean suspended;
	
	/**
	 * Percussion channel
	 */
	private AudioPlayerChannel percussionChannel;
	
	/**
	 * Guaranteed percussion track
	 */
	private Track percussionTrack;
	
	/**
	 * Creates a new AudioPlayer, contructed around a specific song.
	 * 
	 * @param songModel The song to tailor the AudioPlayer to.
	 * @param acceptedTracks Tracks for which to play the audio. Passing null results in all notes being played.
	 */
	public AudioPlayer(SongModel songModel,List<Track> acceptedTracks)
	{
		// initialize relation map
		this.channelMap=new HashMap<Track,AudioPlayerChannel>();
		
		// get channels
		MidiChannel[] channels=synth.getChannels();
		
		// stop anything currently playing
		for (int i=0; i<channels.length; i++)
		{
			channels[i].allSoundOff();
		}
		
		// match channels to AudioPlayerChannel objects, but don't add the active track
		int currentChannel=0;
		this.percussionChannel=new AudioPlayerChannel(channels[PERCUSSION_INDEX]);
		
		this.percussionTrack=new Track("Reserved Percussion",0);
		this.channelMap.put(this.percussionTrack,this.percussionChannel);
		
		// start off assuming we arent't suspended
		this.suspended=false;
		
		for (Track track : acceptedTracks)
		{
			System.out.println(track.getName());
		}
		
		for (Track track : songModel.getTracks())
		{
			// don't add unaccepted tracks, or an empty track
			if ((acceptedTracks==null || acceptedTracks.contains(track)) && track.getNotes().size()>0)
			{
				if (currentChannel<channels.length)
				{
					if (currentChannel==PERCUSSION_INDEX)
					{
						currentChannel++;
					}
					if (track.getVoice()<0)
					{
						channelMap.put(track,percussionChannel);
						if (DEBUG) System.out.println(track.getName()+" -> Reserved as percussion");
					}
					else
					{
						Instrument instrument;
						try
						{
							
							instrument=instrumentList[track.getVoice()];
							if (DEBUG) System.out.println(track.getName()+" -> "+instrument.getName());
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							instrument=instrumentList[0]; // this is probably drums
							if (DEBUG) System.out.println("Defaulting instrument for "+track.getName());
						}
						
						MidiChannel channel=channels[currentChannel];
						channel.programChange(instrument.getPatch().getBank(),instrument.getPatch().getProgram());
						
						AudioPlayerChannel playerChannel=new AudioPlayerChannel(channel);
						
						// add it to the map
						this.channelMap.put(track,playerChannel);
						
						// don't forget to increment this... pretty sure someone did once
						currentChannel++;
					}
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
	public long getLatency()
	{
		// the division converts microseconds to milliseconds
		return synth.getLatency()/1000;
	}
	
	public Track getMetronomeTrack()
	{
		return this.percussionTrack;
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
		
		AudioPlayerChannel channel;
		
		channel=this.channelMap.get(track);
		
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
		else
		{
			if (DEBUG) System.out.println("Could not find channel for "+track.getName());
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
