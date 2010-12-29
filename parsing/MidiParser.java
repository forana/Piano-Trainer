package crescendo.base.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.base.song.Creator;
import crescendo.base.song.TimeSignature;
///import crescendo.base.song.modifier.Tie;
import crescendo.base.song.modifier.Chord;

public class MidiParser implements SongFileParser
{
	public SongModel parse(File file) throws IOException
	{
		MidiInputStream stream=new MidiInputStream(new FileInputStream(file));
		
		// initialize track items - these cannot be track objects yet as they need to be built
		List<List<SkeletalNote>> trackNotes; // this should REALLY be an array, but java can't do that
		String[] trackNames;
		int[] trackVoices;
		// unfortunately this is the best we can do for these
		String author=null;
		String email=null;
		String website=null;
		String license=null;
		// bpm defaults to 120
		int bpm=120;
		// time signature defaults to 4/4
		TimeSignature timeSignature=new TimeSignature(4,4);
		
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
		double framesPerSecond=timeDivisor>>8 & 0x7F;
		if (framesPerSecond==29)
		{
			framesPerSecond=29.97;
		}
		int ticksPerFrame=timeDivisor & 0x00FF;
		// each track header
		for (int i=0; i<numTracks; i++)
		{
			if (stream.readBytes(4)!=0x4D54726B) // MTrk
			{
				throw new IOException("Track header not found");
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
					bytesRead++;
					boolean dontReadInStop=false;
					int notePitch=0;
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
							}
							// if this pitch is held by a note, well golly, let's add that sucker
							for (Iterator<SkeletalNote> iter=activeNotes.iterator(); iter.hasNext();)
							{
								SkeletalNote note=iter.next();
								if (note.getPitch()==notePitch)
								{
									double numbeats;
									if (timeMode==0)
									{
										numbeats=1.0*note.getDuration()/ticksPerBeat;
									}
									else
									{
										double frames=note.getDuration()/ticksPerFrame;
										double seconds=frames/framesPerSecond;
										numbeats=seconds*bpm/60;
									}
									note.setNumBeats(numbeats);
									iter.remove();
									trackNotes.get(i).add(note);
								}
							}
							break;
						case 0xA: // Note aftertouch
							// TODO handle aftertouch w/ modifier
							/*int noteAftertouchPitch=*/stream.read();
							/*int noteAftertouchAmount=*/stream.read();
							bytesRead+=2;
							break;
						case 0xB: // Controller event
							// http://wiki.cockos.com/wiki/index.php/MIDI_Specification
							// TODO handle this?
							/*int controllerType=*/stream.read();
							/*int controllerValue=*/stream.read();
							bytesRead+=2;
							break;
						case 0xC: // Program change
							int programNumber=stream.read();
							bytesRead++;
							// set the current track's voice
							trackVoices[i]=programNumber;
							break;
						case 0xD: // Channel aftertouch
							// TODO if we handle aftertouch we need to handle this as well
							/*int aftertouchAmount=*/stream.read();
							bytesRead++;
							break;
						case 0xE: // Pitch bend
							/*int bendLSB=*/stream.read();
							/*int bendMSB=*/stream.read();
							/*int bendAmount=(bendMSB<<7)+bendLSB;*/
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
								switch (metaCode)
								{
									case 0x00: // Sequence number
										// not handling - sequence number is irrelevant
										/*int sequenceNumber=*/stream.readBytes(2);
										break;
									case 0x01: // Text event
										// not handling
										/*String eventText=*/stream.readString(metaLength);
										break;
									case 0x02: // Copyright notice
										String copyright=stream.readString(metaLength);
										license=copyright;
										break;
									case 0x03: // Sequence / track name
										String trackName=stream.readString(metaLength);
										trackNames[i]=trackName;
										break;
									case 0x04: // Instrument name
										// TODO should handle this so that user has an idea of what instrument the track is
										/*String instrumentName=*/stream.readString(metaLength);
										break;
									case 0x05: // Lyrics
										// not handling
										/*String lyrics=*/stream.readString(metaLength);
										break;
									case 0x06: // Marker
										// TODO Actually use markers?
										/*String markerName=*/stream.readString(metaLength);
										break;
									case 0x07: // Cue point
										// same as marker?
										/*String cueName=*/stream.readString(metaLength);
										break;
									case 0x20: // Midi channel prefix
									case 0x21: // Midi port prefix
										       // found at http://www.omega-art.com/midi/mfiles.html
										/*int prefixChannel=*/stream.read();
										break;
									case 0x2F: // End of track
										// TODO handle this
										break;
									case 0x51: // Set tempo
										int microSecondsPerQuarterNote=stream.readBytes(3);
										int microSecondsPerMinute=60000000;
										bpm=microSecondsPerMinute/microSecondsPerQuarterNote;
										break;
									case 0x54: // SMPTE offset
										/*int hours=*/stream.read();
										/*int minutes=*/stream.read();
										/*int seconds=*/stream.read();
										/*int frames=*/stream.read();
										/*int subFrames=*/stream.read();
										// TODO handle this
										break;
									case 0x58: // Time signature
										int numerator=stream.read();
										int denominator=(int)Math.pow(2,stream.read());
										/*int clockCyclesPerMetronomeTick=*/stream.read();
										/*int thirtySecondNotesPerQuarterNote=*/stream.read(); // for sanity's sake, not using this
										timeSignature=new TimeSignature(numerator,denominator);
										break;
									case 0x59: // Key signature
										/*int keySignature=*/stream.read();//-7;
										/*int scale=*/stream.read();
										break;
									case 0x7F: // Sequencer-specific event
										/*String miscData=*/stream.readString(metaLength);
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
							}
							break;
						default:
							// stupid note repetition
							if (opcode==0x0A) // start repetition mode?
							{
								stream.read(); // no idea what this parameter is
								bytesRead++;
								break;
							}
							else//if (lastOpcode>>4==0x9) // was our last non-repetition opcode note on?
							{
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
		for (int i=0; i<numTracks; i++)
		{
			/*if (trackNotes.get(i).size()==0)
			{
				continue;
			}*/
			Track track=new Track(trackNames[i],trackVoices[i]);
			System.out.println(trackNames[i]);
			int j=0;
			List<SkeletalNote> snotes=trackNotes.get(i);
			// normalize note lengths
			this.normalize(snotes);
			long currentTime=0;
			while (j<snotes.size())
			{
				SkeletalNote current=snotes.get(j);
				// if there's a difference, a rest needs to be added
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
					track.addNote(rest);
				}
				//find all notes that start at the same time
				List<SkeletalNote> simultaneousNotes=new LinkedList<SkeletalNote>();
				while (j+1<snotes.size() && snotes.get(j+1).getOffset()==current.getOffset())
				{
					simultaneousNotes.add(snotes.get(j+1));
					j++;
				}
				if (simultaneousNotes.size()>0)
				{
					//if they all end at the same time, it's a chord, otherwise it's a bunch of ties
					//boolean chord=true;
					/*for (SkeletalNote individual:simultaneousNotes)
					{
						if (individual.getDuration()!=current.getDuration())
						{
							chord=false;
							break;
						}
					}*/
					//if (chord)
					//{
						Note note=current.getNote(track);
						List<Note> chordNotes=new LinkedList<Note>();
						chordNotes.add(note);
						for (SkeletalNote skel:simultaneousNotes)
						{
							chordNotes.add(skel.getNote(track));
						}
						note.addModifier(new Chord(chordNotes));
						track.addNote(note);
					//}
					/*else
					{
						simultaneousNotes.add(current);
						// tie each note to the start note
						for (SkeletalNote skel:simultaneousNotes)
						{
							//note.addModifier(new Tie(note,skel.getNote(track)));
						}
					}*/
				}
				else
				{
					track.addNote(current.getNote(track));
				}
				currentTime=current.getOffset()+current.getDuration();
				j++;
			}
			tracks.add(track);
		}
		
		List<Creator> creators=new LinkedList<Creator>();
		creators.add(new Creator(author,"Sequencer"));
		
		String title=file.getName();
		if (tracks.size()>0 && tracks.get(0).getName()!="")
		{
			title=tracks.get(0).getName()+" ("+title+")";
		}
		
		SongModel model=new SongModel(tracks,title,creators,email,website,license,bpm,timeSignature);
		return model;
	}
	
	/**
	 * Normalizes the note lengths in a list of skeletal notes so that they correspond to normal note lengths.
	 * @param notes
	 */
	private void normalize(List<SkeletalNote> notes)
	{
		// this means getting to the closest precision we can
		for (SkeletalNote note : notes)
		{
			// for the purposes of getting most things right we're going to assume that if something wants 32nd notes
			//    it will have specified them precisely, and otherwise we're rounding to the nearest 16th
			// one 31nd beat is an 8th of a beat
			double testNum=note.getNumBeats()*8;
			if ((int)testNum==testNum)
			{
				// brief step back here:
				// what this is doing is saying that if the number of beats * 8 is an integer,
				// then this note needs no adjusting. the side effect of this is that notes with
				// factors of 8 as the beat divider (half, eighth, sixteenth, quarter, whole) also
				// get skipped in this test, and that's ok
				continue;
			}
			// do the same thing for triplets (only quarter note triplets)
			testNum=note.getNumBeats()*3;
			if ((int)testNum==testNum)
			{
				continue;
			}
			// round to the nearest 16th (or 4th of a quarter), the fun way
			double newLength=Math.round(4*note.getNumBeats())/4.0;
			note.setNumBeats(newLength);
		}
	}
	
	private class SkeletalNote
	{
		private int pitch;
		private int velocity;
		private int duration;
		private long offset;
		private double numBeats;
		
		public SkeletalNote(int pitch,int velocity,long currentDelta)
		{
			this.pitch=pitch;
			this.velocity=velocity;
			this.duration=0;
			this.offset=currentDelta;
			this.numBeats=-1;
		}
		
		public int getPitch()
		{
			return this.pitch;
		}
		
		public int getDuration()
		{
			return this.duration;
		}
		
		public void addDuration(int delta)
		{
			this.duration+=delta;
		}
		
		public long getOffset()
		{
			return this.offset;
		}
		
		public double getNumBeats()
		{
			return this.numBeats;
		}
		
		public void setNumBeats(double numBeats)
		{
			this.numBeats=numBeats;
		}
		
		public Note getNote(Track track)
		{
			System.out.println("Beats: "+this.numBeats);
			return new Note(this.pitch,this.numBeats,this.velocity,track);
		}
	}
}
