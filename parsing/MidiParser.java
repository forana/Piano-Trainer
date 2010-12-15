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
		String title=file.getName();
		String author=null;
		String email=null;
		String website=null;
		String license=null;
		// bpm defaults to 120
		int bpm=120;
		
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
			trackNotes.add(new LinkedList<SkeletalNote>());
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
							System.out.println("\nNote on "+notePitch);
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
									iter.remove();
									trackNotes.get(i).add(note);
								}
							}
							break;
						case 0xA: // Note aftertouch
							int noteAftertouchPitch=stream.read();
							int noteAftertouchAmount=stream.read();
							bytesRead+=2;
							break;
						case 0xB: // Controller event
							// http://wiki.cockos.com/wiki/index.php/MIDI_Specification
							int controllerType=stream.read();
							int controllerValue=stream.read();
							bytesRead+=2;
							break;
						case 0xC: // Program change
							int programNumber=stream.read();
							bytesRead++;
							// set the current track's voice
							trackVoices[i]=programNumber;
							break;
						case 0xD: // Channel aftertouch
							int aftertouchAmount=stream.read();
							bytesRead++;
							break;
						case 0xE: // Pitch bend
							int bendLSB=stream.read();
							int bendMSB=stream.read();
							int bendAmount=bendMSB<<7+bendLSB;
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
										int sequenceNumber=stream.readBytes(2);
										break;
									case 0x01: // Text event
										String eventText=stream.readString(metaLength);
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
										String instrumentName=stream.readString(metaLength);
										break;
									case 0x05: // Lyrics
										String lyrics=stream.readString(metaLength);
										break;
									case 0x06: // Marker
										// TODO Actually use markers?
										String markerName=stream.readString(metaLength);
										break;
									case 0x07: // Cue point
										String cueName=stream.readString(metaLength);
										break;
									case 0x20: // Midi channel prefix
									case 0x21: // Midi port prefix
										       // found at http://www.omega-art.com/midi/mfiles.html
										int prefixChannel=stream.read();
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
										int hours=stream.read();
										int minutes=stream.read();
										int seconds=stream.read();
										int frames=stream.read();
										int subFrames=stream.read();
										break;
									case 0x58: // Time signature
										int numerator=stream.read();
										int denominator=(int)Math.pow(2,stream.read());
										System.out.println("set time to "+numerator+"/"+denominator);
										int clockCyclesPerMetronomeTick=stream.read();
										int thirtySecondNotesPerQuarterNote=stream.read();
										break;
									case 0x59: // Key signature
										int keySignature=stream.read()-7;
										int scale=stream.read();
										break;
									case 0x7F: // Sequencer-specific event
										String miscData=stream.readString(metaLength);
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
								stream.read(); // no idea what this parameter is, but it's usually 0x3F
								bytesRead++;
							}
							else//if (lastOpcode>>4==0x9) // was our last non-repetition opcode note on?
							{
								stream.push(opcode); // because this isn't really an opcode, it's a parameter
								stream.push(lastOpcode); // just do the same thing again
								repeatOpcode=true;
								bytesRead-=2;
								continue;
							}
							//else 
							/*else if (opcode==0x00) // stop repetition mode?
							{
								stream.push(opcode); // this is actually the next event's delta
								bytesRead--;
								break;
							}*/
							/*else
							{
								System.out.println("\n"+Integer.toHexString(lastOpcode));
								throw new IOException("Unrecognized opcode '"+Integer.toHexString(opcode>>4)+"' @ track "+(i+1)+" offset 0x"+Integer.toHexString(bytesRead));
							}*/
					}
					lastOpcode=opcode;	
				} while (repeatOpcode);
			}
		}
		
		
		List<Track> tracks=new ArrayList<Track>();
		for (int i=0; i<numTracks; i++)
		{
			Track track=new Track(trackNames[i],trackVoices[i]);
			List<Note> notes=new LinkedList<Note>();
			System.out.println("Track "+(i+1)+" - "+trackNames[i]);
			for (Iterator<SkeletalNote> iter=trackNotes.get(i).iterator(); iter.hasNext();)
			{
				SkeletalNote snote=iter.next();
				double numbeats;
				if (timeMode==0)
				{
					numbeats=1.0*snote.getDuration()/ticksPerBeat;
					//System.out.println("Ticks: "+snote.getDuration()+"\t / "+ticksPerBeat);
				}
				else
				{
					double frames=snote.getDuration()/ticksPerFrame;
					double seconds=frames/framesPerSecond;
					numbeats=seconds*bpm/60;
				}
				System.out.println("\tDelta: "+snote.getOffset());
				System.out.println("\tBeats: "+numbeats+" ("+snote.getDuration()+")");
			}
			tracks.add(track);
		}
		System.out.println(bpm);
		System.out.println(ticksPerBeat);
		
		List<Creator> creators=new LinkedList<Creator>();
		creators.add(new Creator(author,"Sequencer"));
		
		SongModel model=new SongModel(tracks,title,creators,email,website,license,bpm,null);
		return model;
	}
	
	private class SkeletalNote
	{
		private int pitch;
		private int velocity;
		private int duration;
		private long offset;
		
		public SkeletalNote(int pitch,int velocity,long currentDelta)
		{
			this.pitch=pitch;
			this.velocity=velocity;
			this.duration=0;
			this.offset=currentDelta;
		}
		
		public int getPitch()
		{
			return this.pitch;
		}
		
		public int getVelocity()
		{
			return this.velocity;
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
	}
}
