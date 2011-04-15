package crescendo.base.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.base.song.Creator;
import crescendo.base.song.TimeSignature;
import crescendo.base.song.modifier.NoteModifier;
//import crescendo.base.song.modifier.Tie;
import crescendo.base.song.modifier.Chord;
import crescendo.base.song.modifier.Modulation;
import crescendo.base.song.modifier.TimeSignatureChange;
import crescendo.base.song.modifier.TempoChange;

/**
 * Provides a parser for MIDI files, supporting basic note functionality.
 * 
 * @author forana
 */
public class MidiParser implements SongFileParser
{
	private static final boolean DEBUG = false;
	private static final boolean NORMALIZE = true;
	
	// this is set according to the midi spec
	private static final int PERCUSSION_CHANNEL_INDEX=9;
	
	/**
	 * Create a SongModel from the given file.
	 * 
	 * @param file The file to parse.
	 * 
	 * @throw IOException if the file is improperly formatted.
	 */
	public SongModel parse(File file) throws IOException
	{
		MidiInputStream stream=new MidiInputStream(new FileInputStream(file));
		
		// song name
		String songName=null;
		// initialize track items - these cannot be track objects yet as they need to be built
		List<List<SkeletalNote>> trackNotes; // this should REALLY be an array, but java can't do that
		String[] trackNames;
		int[] trackVoices;
		List<SkeletalNote> percussionNotes;
		List<TimedModifier> modifiers;
		// unfortunately this is the best we can do for these
		String author="Unknown";
		String license=null;
		// bpm defaults to 120
		int bpm=120;
		// time signature defaults to 4/4
		TimeSignature timeSignature=null;
		// key signature defaults to 0 (no accidentals)
		int keySignature=0;
		
		// according to specification at http://www.sonicspot.com/guide/midifiles.html
		// main header
		if (stream.readBytes(4)!=0x4D546864) // MThd
		{
			throw new IOException("Missing header identifier");
		}
		if (stream.readBytes(4)!=6) // always 6
		{
			throw new IOException("Improper header chunk size");
		}
		int formatType=stream.readBytes(2);
		if (formatType!=0 && formatType!=1)
		{
			throw new IOException("MIDI type not accepted in this application");
		}
		int numTracks=stream.readBytes(2);
		trackNotes=new ArrayList<List<SkeletalNote>>(numTracks);
		trackNames=new String[numTracks];
		trackVoices=new int[numTracks];
		percussionNotes=new ArrayList<SkeletalNote>();
		modifiers=new ArrayList<TimedModifier>();
		for (int i=0; i<numTracks; i++)
		{
			trackNotes.add(new ArrayList<SkeletalNote>()); // arraylist to make it faster to access via index
			trackNames[i]="";
			trackVoices[i]=0;
		}
		int timeDivisor=stream.readBytes(2);
		// mode zero means that the divisor is ticks per beat (time is always 4/4)
		// mode one means that the divisor's top 7 bits is the number of frames per second
		//  and the bottom 8 bits are the number of ticks per frame (subdivisions)
		int timeMode=((timeDivisor & 0x8000) == 0 ? 0 : 1); // stupid conversion but it works
		int ticksPerBeat=timeDivisor & 0x7FFF;
		double framesPerSecond=(timeDivisor>>8) & 0x7F;
		if (framesPerSecond==29)
		{
			framesPerSecond=29.97;
		}
		int ticksPerFrame=timeDivisor & 0x00FF;
		// each track header
		for (int i=0; i<numTracks; i++)
		{
			int header=stream.readBytes(4);
			if (header!=0x4D54726B) // MTrk
			{
				throw new IOException("Track header not found (track #"+(i+1)+": 0x"+Integer.toHexString(header)+")");
			}
			// this map will contain all active notes, apply modifiers to them, etc
			List<SkeletalNote> activeNotes=new LinkedList<SkeletalNote>();
			int trackLength=stream.readBytes(4);
			int bytesRead=0;
			long currentDelta=0;
			int lastOpcode=0;
			while (bytesRead<trackLength)
			{
				int deltaTime=stream.readVariableWidth();
				bytesRead+=stream.lastVariableLength();
				// add to the time of all active notes
				for (SkeletalNote note:activeNotes)
				{
					note.addDuration(deltaTime);
				}
				currentDelta+=deltaTime;
				boolean repeatOpcode;
				do
				{
					repeatOpcode=false;
					int opcode=stream.read();
					int channel=opcode & 0x0F;
					if (DEBUG) 	System.out.print(Integer.toHexString(channel)+"\t");
					bytesRead++;
					boolean dontReadInStop=false;
					int notePitch=0;
					if (DEBUG)
					{
						System.out.print(deltaTime+"\t "+Integer.toHexString(opcode>>4)+"\t");
					}
					switch (opcode>>4)
					{
						case 0x9: // Note on
							notePitch=stream.read();
							int noteOnVelocity=stream.read();
							bytesRead+=2;
							if (noteOnVelocity>0)
							{
								boolean found=false;
								// don't add a note if there's already a note there
								for (SkeletalNote note:activeNotes)
								{
									if (note.getPitch()==notePitch)
									{
										found=true;
										break;
									}
								}
								if (!found)
								{
									// use 0 for duration and null for track for now; these will be added later
									activeNotes.add(new SkeletalNote(notePitch,noteOnVelocity,currentDelta));
								}
								if (DEBUG)
								{
									System.out.println(notePitch+" @ "+noteOnVelocity);
								}
								break;
							}
							else
							{
								dontReadInStop=true;
							}
						case 0x8: // Note off
							if (!dontReadInStop)
							{
								notePitch=stream.read();
								/*int noteOffVelocity=*/stream.read(); // this should always be zero, but if it isn't, we don't really care
								bytesRead+=2;
								if (DEBUG)
								{
									System.out.println(notePitch);
								}
							}
							// if this pitch is held by a note, well golly, let's add that sucker
							for (Iterator<SkeletalNote> iter=activeNotes.iterator(); iter.hasNext();)
							{
								SkeletalNote note=iter.next();
								if (note.getPitch()==notePitch)
								{
									iter.remove();
									// if it's in the percussion channel, add it to that list instead
									if (channel==PERCUSSION_CHANNEL_INDEX)
									{
										percussionNotes.add(note);
									}
									else
									{
										trackNotes.get(i).add(note);
									}
								}
							}
							break;
						case 0xA: // Note aftertouch
							// note handling aftertouch
							/*int noteAftertouchPitch=*/stream.read();
							/*int noteAftertouchAmount=*/stream.read();
							if (DEBUG) System.out.println("(aftertouch)");
							bytesRead+=2;
							break;
						case 0xB: // Controller event
							// http://wiki.cockos.com/wiki/index.php/MIDI_Specification
							// not handling controller event
							/*int controllerType=*/stream.read();
							/*int controllerValue=*/stream.read();
							if (DEBUG) System.out.println("(controller event)");
							bytesRead+=2;
							break;
						case 0xC: // Program change
							int programNumber=stream.read();
							bytesRead++;
							// set the current track's voice
							if (channel!=PERCUSSION_CHANNEL_INDEX)
							{
								trackVoices[i]=programNumber;
							}
							if (DEBUG) System.out.println("(voice changed)");
							break;
						case 0xD: // Channel aftertouch
							// not handling aftertouch
							/*int aftertouchAmount=*/stream.read();
							bytesRead++;
							if (DEBUG) System.out.println("(channel aftertouch)");
							break;
						case 0xE: // Pitch bend
							/*int bendLSB=*/stream.read();
							/*int bendMSB=*/stream.read();
							/*int bendAmount=(bendMSB<<7)+bendLSB;*/
							if (DEBUG) System.out.println("(pitch was bent)");
							bytesRead+=2;
							break;
						case 0xF: // Meta event
							if (channel==0xF)
							{
								int metaCode=stream.read();
								bytesRead++;
								int metaLength=stream.readVariableWidth();
								bytesRead+=stream.lastVariableLength();
								// this should probably not be done here (rather in each branch), but lazyness
								bytesRead+=metaLength;
								if (DEBUG) System.out.print(metaCode+"\t");
								switch (metaCode)
								{
									case 0x00: // Sequence number
										// not handling - sequence number is irrelevant
										int sequenceNumber=stream.readBytes(metaLength);
										if (DEBUG) System.out.println("changed sequence: "+sequenceNumber);
										break;
									case 0x01: // Text event
										// not handling
										String eventText=stream.readString(metaLength);
										if (DEBUG) System.out.println("read text: "+eventText);
										break;
									case 0x02: // Copyright notice
										String copyright=stream.readString(metaLength);
										license=copyright;
										if (DEBUG) System.out.println("Copyright: "+copyright);
										break;
									case 0x03: // Sequence / track name
										String trackName=stream.readString(metaLength);
										trackNames[i]=trackName;
										if (songName==null)
										{
											songName=trackName;
										}
										if (DEBUG) System.out.println("Track name: "+trackName);
										break;
									case 0x04: // Instrument name
										// TODO should handle this so that user has an idea of what instrument the track is
										/*String instrumentName=*/stream.readString(metaLength);
										if (DEBUG) System.out.println("named the instrument");
										break;
									case 0x05: // Lyrics
										// not handling
										/*String lyrics=*/stream.readString(metaLength);
										if (DEBUG) System.out.println("lyrics happened");
										break;
									case 0x06: // Marker
										// not handling markers
										String markerName=stream.readString(metaLength);
										if (DEBUG) System.out.println("read a marker: "+markerName);
										break;
									case 0x07: // Cue point
										// not handling markers
										String cueName=stream.readString(metaLength);
										if (DEBUG) System.out.println("read a cue point: "+cueName);
										break;
									case 0x20: // Midi channel prefix
									case 0x21: // Midi port prefix
										       // found at http://www.omega-art.com/midi/mfiles.html
										/*int prefixChannel=*/stream.read();
										if (DEBUG) System.out.println("midi port prefixed");
										break;
									case 0x2F: // End of track
										// to handle this would just break things more; use for debug only
										if (DEBUG) System.out.println("end of track");
										break;
									case 0x51: // Set tempo
										int microSecondsPerQuarterNote=stream.readBytes(3);
										int microSecondsPerMinute=60000000;
										int nbpm=microSecondsPerMinute/microSecondsPerQuarterNote;
										if (currentDelta>0)
										{
											modifiers.add(new TimedModifier(currentDelta,new TempoChange(nbpm)));
										}
										else
										{
											bpm=nbpm;
										}
										if (DEBUG) System.out.println(bpm+" bpm");
										break;
									case 0x54: // SMPTE offset
										/*int hours=*/stream.read();
										/*int minutes=*/stream.read();
										/*int seconds=*/stream.read();
										/*int frames=*/stream.read();
										/*int subFrames=*/stream.read();
										// TODO handle this
										if (DEBUG) System.out.println("did an SMPTE offset");
										break;
									case 0x58: // Time signature
										int numerator=stream.read();
										int denominator=(int)Math.pow(2,stream.read());
										/*int clockCyclesPerMetronomeTick=*/stream.read();
										/*int thirtySecondNotesPerQuarterNote=*/stream.read(); // for sanity's sake, not using this
										if (timeSignature==null)
										{
											timeSignature=new TimeSignature(numerator,denominator);
										}
										else
										{
											modifiers.add(new TimedModifier(currentDelta,new TimeSignatureChange(
												new TimeSignature(numerator,denominator))));
										}
										if (DEBUG) System.out.println("set time to "+numerator+"/"+denominator);
										break;
									case 0x59: // Key signature
										int nkeySignature=stream.read();
										if (nkeySignature>127)
										{
											nkeySignature=-(255-nkeySignature);
										}
										if (currentDelta>0)
										{
											modifiers.add(new TimedModifier(currentDelta,new Modulation(nkeySignature)));
										}
										else
										{
											keySignature=nkeySignature;
										}
										/*int scale=*/stream.read();
										if (DEBUG) System.out.println("set key signature to "+keySignature);
										break;
									case 0x7F: // Sequencer-specific event
										/*String miscData=*/stream.readString(metaLength);
										if (DEBUG) System.out.println("some sequencer-specific event");
										break;
									default:
										throw new IOException("Unrecognized meta event code '"+Integer.toHexString(metaCode)+"' @ track "+(i+1)+" offset "+bytesRead);
								}
							}
							else // manufacturer-exclusive event, aka we don't care
							{
								int metaLength=stream.readVariableWidth();
								bytesRead+=stream.lastVariableLength();
								// just read the proper number of bytes so we can all go home
								stream.readBytes(metaLength);
								bytesRead+=metaLength;
								if (DEBUG) System.out.println("manufacturer exclusive");
							}
							break;
						default:
							// stupid note repetition
							/*if (opcode==0x0A) // start repetition mode?
							{
								if (DEBUG) System.out.println("It's that thing that I have no idea what it does.");
								stream.read(); // no idea what this parameter is
								bytesRead++;
								break;
							}
							else//if (lastOpcode>>4==0x9) // was our last non-repetition opcode note on?
							*/{
								if (DEBUG) System.out.println("(repetition)");
								stream.push(opcode); // because this isn't really an opcode, it's a parameter
								stream.push(lastOpcode); // just do the same thing again
								repeatOpcode=true;
								bytesRead-=2;
								continue; // don't set lastOpcode
							}
					}
					lastOpcode=opcode;	
				} while (repeatOpcode);
			}
		}
		
		
		List<Track> tracks=new ArrayList<Track>();
		// add in percussion
		trackNotes.add(percussionNotes);
		
		for (int i=0; i<trackNotes.size(); i++)
		{
			// skip empty tracks?
			if (trackNotes.get(i).size()==0)
			{
				if (DEBUG) System.out.println("Skipping empty track "+i);
				continue;
			}
			List<SkeletalNote> snotes=trackNotes.get(i);
			// sort the notes by offset
			Collections.sort(snotes);
			
			int modIndex=0;
			int cbpm=bpm;
			long currentTime=0;
			long endOfSong=0;
			// loop #1: normalize the lengths of all notes and collect some info
			for (SkeletalNote note : snotes)
			{
				// make sure we're calculating with the right bpm
				while (modIndex<modifiers.size() && currentTime<=modifiers.get(modIndex).getTime())
				{
					NoteModifier mod=modifiers.get(modIndex).getModifier();
					if (mod instanceof TempoChange)
					{
						cbpm=((TempoChange)mod).getTargetTempo();
						if (DEBUG) System.out.println("\tBPM = "+cbpm);
					}
					modIndex++;
				}
				
				note.setNumBeats(timeMode,ticksPerBeat,ticksPerFrame,framesPerSecond,cbpm);
				
				// update where we are
				currentTime=note.getOffset()+note.getDuration();
				
				// mark end of song
				if (currentTime>endOfSong)
				{
					endOfSong=currentTime;
				}
			}
			
			Track track;
			if (snotes==percussionNotes)
			{
				track=new Track("Percussion",-1); // negative voice for percussion
			}
			else
			{
				track=new Track(trackNames[i],trackVoices[i]);
			}
			if (DEBUG) System.out.println("Track: "+track.getName());
			int stop=snotes.size();
			if (DEBUG) System.out.println("\t"+stop+" notes before split");
			// loop #2 - split up ties
			for (int j=0; j<stop; j++)
			{
				SkeletalNote current=snotes.get(j);
				
				// make sure we're calculating with the right bpm
				while (modIndex<modifiers.size() && currentTime<=modifiers.get(modIndex).getTime())
				{
					NoteModifier mod=modifiers.get(modIndex).getModifier();
					if (mod instanceof TempoChange)
					{
						cbpm=((TempoChange)mod).getTargetTempo();
						if (DEBUG) System.out.println("\tBPM = "+cbpm);
					}
					modIndex++;
				}
				
				// add in divisions
				int k=j+1;
				while (k<stop)
				{
					SkeletalNote pnote=snotes.get(k);
					// if the note is too far ahead, stop looking ahead
					if (pnote.getOffset()-current.getOffset()>=current.getDuration())
					{
						break;
					}
					// make a division at the start and end of the other note
					current.addDivision(pnote.getOffset());
					current.addDivision(pnote.getOffset()+pnote.getDuration());
					// make a division in the other note at the end of this note - covers the S-block issue (think tetris)
					pnote.addDivision(current.getOffset()+current.getDuration());
					// the rest will be handled by the note itself
					k++;
				}
				// split divided notes
				snotes.addAll(current.split(timeMode,ticksPerBeat,ticksPerFrame,framesPerSecond,cbpm));
				
				// update where we are
				currentTime=current.getOffset()+current.getDuration();
			}
			
			// sort again, because we added notes (possibly)
			Collections.sort(snotes);
			if (DEBUG) System.out.println("\t"+snotes.size()+" notes after split");
			
			// loop #3, let's chord it up and make real notes out of it
			List<Note> notes=new LinkedList<Note>();
			modIndex=0;
			currentTime=0;
			cbpm=bpm;
			for (int j=0; j<snotes.size(); )
			{
				while (modIndex<modifiers.size() && currentTime<=modifiers.get(modIndex).getTime())
				{
					NoteModifier mod=modifiers.get(modIndex).getModifier();
					if (mod instanceof TempoChange)
					{
						cbpm=((TempoChange)mod).getTargetTempo();
					}
					modIndex++;
				}
				
				SkeletalNote current=snotes.get(j);
				
				// add a rest if needed
				if (current.getOffset()!=currentTime)
				{
					long difference=current.getOffset()-currentTime;
					double restBeats;
					if (timeMode==0)
					{
						restBeats=1.0*difference/ticksPerBeat;
					}
					else
					{
						double frames=difference/ticksPerFrame;
						double seconds=frames/framesPerSecond;
						restBeats=seconds*bpm/60;
					}
					// a rest's pitch and velocity don't matter, but it isn't playable
					Note rest=new Note(0,restBeats,0,track,false);
					notes.add(rest);
				}
				
				// turn into real note
				Note note=current.getNote(track);
				
				// find simultaneous notes
				j++;
				List<SkeletalNote> simultaneousNotes=new LinkedList<SkeletalNote>();
				while (j<snotes.size() && snotes.get(j).getOffset()==current.getOffset())
				{
					simultaneousNotes.add(snotes.get(j));
					j++;
				}
				
				// if any found, add them as modifier to this note
				if (simultaneousNotes.size()>0)
				{
					List<Note> chordNotes=new LinkedList<Note>();
					for (SkeletalNote snote : simultaneousNotes)
					{
						chordNotes.add(snote.getNote(track));
					}
					note.addModifier(new Chord(chordNotes));
				}
				
				notes.add(note);
				
				currentTime=current.getOffset()+current.getDuration();
			}
			
			// if there was time left over after that iteration, it should be a rest
			if (currentTime<endOfSong)
			{
				long difference=endOfSong-currentTime;
				double restBeats;
				if (timeMode==0)
				{
					restBeats=1.0*difference/ticksPerBeat;
				}
				else
				{
					double frames=difference/ticksPerFrame;
					double seconds=frames/framesPerSecond;
					restBeats=seconds*bpm/60;
				}
				// a rest's pitch and velocity don't matter, but it isn't playable
				Note rest=new Note(0,restBeats,0,track,false);
				notes.add(rest);
			}
			
			// TODO add tie checking loop here
			
			if (DEBUG) System.out.println("\t"+notes.size()+" notes after rests added");
			
			// loop #5: normalize and add all notes to track
			for (Note note : notes)
			{
				if (NORMALIZE)
				{
					if (DEBUG)
					{
						System.out.println("\t(before normalization)");
						System.out.println("\t"+note.getPitch()+" @\t"+note.getDuration()+" beats");
						System.out.println("\t(after)");
					}
					double duration=note.getDuration();
					if (Math.floor(duration*3)!=duration*3           // triplet check
						&& Math.floor(duration*4)!=duration*4)       // sixteenth check
					{
						// note is not a triplet nor a multiple of a sixteenth note
						Note oldNote=note;
						note=new Note(note.getPitch(),Math.round(duration*4)/4.0,note.getDynamic(),note.getTrack(),note.isPlayable());
						// also add all modifiers that were there / round their notes as well
						for (NoteModifier oldMod : oldNote.getModifiers())
						{
							if (oldMod instanceof Chord)
							{
								List<Note> newNotes=new LinkedList<Note>();
								for (Note other : oldMod.getNotes())
								{
									newNotes.add(new Note(other.getPitch(),Math.round(other.getDuration()*4)/4.0,other.getDynamic(),other.getTrack(),other.isPlayable()));
								}
								Chord newChord=new Chord(newNotes);
								note.addModifier(newChord);
							}
							else
							{
								note.addModifier(oldMod);
							}
						}
					}
				}
				if (DEBUG)
				{
					System.out.print("\t"+(note.isPlayable()?"O":"-"));
					System.out.println("\t"+note.getPitch()+" @\t"+note.getDuration()+" beats");
				}
				if (note.getDuration()>0)
				{
					track.addNote(note);
				}
			}
			
			tracks.add(track);
		}
		
		List<Creator> creators=new LinkedList<Creator>();
		creators.add(new Creator(author,"Sequencer"));
		
		String title=songName+" ("+file.getName()+")";
		
		if (timeSignature==null)
		{
			timeSignature=new TimeSignature(4,4);
		}
		
		SongModel model=new SongModel(tracks,title,creators,license,bpm,timeSignature,keySignature);
		return model;
	}
	
	private static class TimedModifier
	{
		private long time;
		private NoteModifier modifier;
		
		public TimedModifier(long time,NoteModifier modifier)
		{
			this.time=time;
			this.modifier=modifier;
		}
		
		public long getTime()
		{
			return time;
		}
		
		public NoteModifier getModifier()
		{
			return modifier;
		}
	}
	
	/**
	 * Provides a container class to build notes in procedurally.
	 * 
	 * @author forana
	 */
	private static class SkeletalNote implements Comparable<SkeletalNote>
	{
		/** Pitch of the note (midi terms). */
		private int pitch;
		
		/** Velocity of the note. */
		private int velocity;
		
		/** Duration of the note, in milliseconds. */
		private int duration;
		
		/** The time in the song at which this event occurs. */
		private long offset;
		
		/** The length of the note in beats. */
		private double numBeats;
		
		/** Set of times at which this note is divided. */
		private Set<Long> divisions;
		
		/**
		 * Creates a new skeletal note.
		 * 
		 * @param pitch The pitch of the note.
		 * @param velocity The velocity of the press.
		 * @param currentDelta The time at which this note begins.
		 */
		public SkeletalNote(int pitch,int velocity,long currentDelta)
		{
			this.pitch=pitch;
			this.velocity=velocity;
			this.duration=0;
			this.offset=currentDelta;
			this.numBeats=-1;
			this.divisions=new HashSet<Long>();
		}
		
		/**
		 * Get the pitch of the note.
		 * 
		 * @return The pitch of the note, in midi terms.
		 */
		public int getPitch()
		{
			return this.pitch;
		}
		
		/** Get the length of the note.
		 * 
		 * @return The length of the note, in milliseconds.
		 */
		public int getDuration()
		{
			return this.duration;
		}
		
		/**
		 * Increase the duration of this note.
		 * 
		 * @param delta The amount to add, in milliseconds.
		 */
		public void addDuration(int delta)
		{
			this.duration+=delta;
		}
		
		/**
		 * Get the start time of this note.
		 * 
		 * @return The start time of this note, in milliseconds.
		 */
		public long getOffset()
		{
			return this.offset;
		}
		
		// set it for the first time (can't be set in constructor because the timing isn't initally known)
		/**
		 * Set the number of beats this note represents. This method assumes the duration is completely built.
		 * 
		 * @param timeMode The mode at which this file calculates time (0 or 1).
		 * @param ticksPerBeat The number of 'ticks' per beat.
		 * @param ticksPerFrame The number of 'ticks' per frame.
		 * @param framesPerSecond The number of frames per second.
		 * @param bpm The number of beats per minute for this note.
		 */
		public void setNumBeats(int timeMode,int ticksPerBeat,int ticksPerFrame,double framesPerSecond,int bpm)
		{
			double numbeats;
			if (timeMode==0)
			{
				numbeats=1.0*this.duration/ticksPerBeat;
			}
			else
			{
				double frames=this.duration/ticksPerFrame;
				double seconds=frames/framesPerSecond;
				numbeats=seconds*bpm/60;
			}
			this.numBeats=numbeats;
		}
		
		/**
		 * Compare one note to another, allowing sorting by offset then pitch.
		 * 
		 * @param other The note to compare to.
		 * 
		 * @return An integer with the same sign as the time difference.
		 */
		public int compareTo(SkeletalNote other)
		{
			return (int)(this.getOffset()-other.getOffset());
		}
		
		/**
		 * Adds a division at an arbitrary point along this note's life. This method assumes the duration has already been
		 * built and normalized.
		 * 
		 * @param offset The position at which to make a division.
		 */
		public void addDivision(long offset)
		{
			// don't add start or endpoints or items outside this note
			if (offset>this.getOffset() && offset<this.getOffset()+this.duration)
			{
				this.divisions.add(offset);
			}
		}
		
		public List<SkeletalNote> split(int timeMode,int ticksPerBeat,int ticksPerFrame,double framesPerSecond,int bpm)
		{
			List<SkeletalNote> splitNotes=new LinkedList<SkeletalNote>();
			
			if (this.divisions.size()>0)
			{
				// sort it so everything isn't ruined - has to be a list for this to happen though
				List<Long> sortedDivisions=new ArrayList<Long>(this.divisions);
				Collections.sort(sortedDivisions);
				
				for (int i=1; i<sortedDivisions.size(); i++)
				{
					// calculate difference in clock ticks
					int cycles;
					if (i==sortedDivisions.size()-1)
					{
						cycles=(int)(this.getDuration()-(sortedDivisions.get(i-1)-this.getOffset()));
					}
					else
					{
						cycles=(int)(sortedDivisions.get(i)-sortedDivisions.get(i-1));
					}
					SkeletalNote note=new SkeletalNote(this.pitch,this.velocity,sortedDivisions.get(i));
					note.addDuration(cycles);
					note.setNumBeats(timeMode,ticksPerBeat,ticksPerFrame,framesPerSecond,bpm);
					splitNotes.add(note);
				}
				
				this.duration=(int)(sortedDivisions.get(0)-this.getOffset());
				this.setNumBeats(timeMode,ticksPerBeat,ticksPerFrame,framesPerSecond,bpm);
			}
			
			return splitNotes;
		}
		
		/**
		 * Gets a full note representation of this skeletal version, associated to a specific track. This Note may have a Tie
		 * modifier attached.
		 * 
		 * @param track The track to associate with.
		 * 
		 * @return The full Note object.
		 */
		public Note getNote(Track track)
		{
			return new Note(this.pitch,this.numBeats,this.velocity,track);
		}
	}
}
