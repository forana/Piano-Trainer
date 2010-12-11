package crescendo.base.parsing;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.base.song.Creator;

public class MidiParser implements SongFileParser
{
	// the byte-length of whatever the last variable-length variable was
	private static int lastVariableLength=0;
	
	public SongModel parse(File file) throws IOException
	{
		InputStream stream=new FileInputStream(file);
		
		// initialize track items - these cannot be track objects yet as they need to be built
		List<List<Note>> trackNotes; // this should REALLY be an array, but java can't do that
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
		if (readBytes(stream,4)!=0x4D546864) // MThd
		{
			throw new IOException("Missing header identifier");
		}
		if (readBytes(stream,4)!=6) // always 6
		{
			throw new IOException("Improper header chunk size");
		}
		int formatType=readBytes(stream,2);
		if (formatType!=0 && formatType!=1)
		{
			throw new IOException("MIDI type not accepted in this application");
		}
		int numTracks=readBytes(stream,2);
		trackNotes=new ArrayList<List<Note>>(numTracks);
		trackNames=new String[numTracks];
		trackVoices=new int[numTracks];
		for (int i=0; i<numTracks; i++)
		{
			trackNotes.add(new LinkedList<Note>());
			trackNames[i]="";
			trackVoices[i]=0;
		}
		int timeDivisor=readBytes(stream,2);
		// mode zero means that the divisor is ticks per beat (time is always 4/4)
		// mode one means that the divisor's top 7 bits is the number of frames per second
		//  and the bottom 8 bits are the number of ticks per frame (subdivisions)
		int timeMode=((timeDivisor & 0x8000) == 0 ? 0 : 1);
		int ticksPerBeat=timeDivisor & 0x7FFF;
		double framesPerSecond=timeDivisor>>8 & 0x7F;
		if (framesPerSecond==29)
		{
			framesPerSecond=29.97;
		}
		int frameSubdivisions=timeDivisor & 0x00FF;
		// each track header
		for (int i=0; i<numTracks; i++)
		{
			if (readBytes(stream,4)!=0x4D54726B) // MTrk
			{
				throw new IOException("Track header not found");
			}
			int trackLength=readBytes(stream,4);
			int bytesRead=0;
			while (bytesRead<trackLength)
			{
				int deltaTime=readVariableWidth(stream);
				bytesRead+=lastVariableLength;
				int opcode=stream.read();
				int channel=opcode & 0x0F;
				bytesRead++;
				switch (opcode>>4)
				{
					case 0x8: // Note off
						int noteOffPitch=stream.read();
						int noteOffVelocity=stream.read();
						bytesRead+=2;
						break;
					case 0x9: // Note on
						int noteOnPitch=stream.read();
						int noteOnVelocity=stream.read();
						bytesRead+=2;
						break;
					case 0xA: // Note aftertouch
						int noteAftertouchPitch=stream.read();
						int noteAftertouchAmount=stream.read();
						bytesRead+=2;
						break;
					case 0xB: // Controller event
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
							int metaLength=readVariableWidth(stream);
							bytesRead+=lastVariableLength;
							// this should probably not be done here (rather in each branch), but lazyness
							bytesRead+=metaLength;
							switch (metaCode)
							{
								case 0x00: // Sequence number
									int sequenceNumber=readBytes(stream,2);
									break;
								case 0x01: // Text event
									String eventText=readString(stream,metaLength);
									break;
								case 0x02: // Copyright notice
									String copyright=readString(stream,metaLength);
									license=copyright;
									break;
								case 0x03: // Sequence / track name
									String trackName=readString(stream,metaLength);
									trackNames[i]=trackName;
									break;
								case 0x04: // Instrument name
									String instrumentName=readString(stream,metaLength);
									break;
								case 0x05: // Lyrics
									String lyrics=readString(stream,metaLength);
									break;
								case 0x06: // Marker
									// TODO Actually use markers?
									String markerName=readString(stream,metaLength);
									break;
								case 0x07: // Cue point
									String cueName=readString(stream,metaLength);
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
									int microSecondsPerQuarterNote=readBytes(stream,3);
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
									int clockCyclesPerMetronomeTick=stream.read();
									int thirtySecondNotesPerQuarterNote=stream.read();
									break;
								case 0x59: // Key signature
									int keySignature=stream.read()-7;
									int scale=stream.read();
									break;
								case 0x7F: // Sequencer-specific event
									String miscData=readString(stream,metaLength);
									break;
								default:
									throw new IOException("Unrecognized meta event code '"+Integer.toHexString(metaCode)+"' @ track "+(i+1)+" offset "+bytesRead);
							}
						}
						else // manufacturer-exclusive event, aka we don't care
						{
							int metaLength=readVariableWidth(stream);
							bytesRead+=lastVariableLength;
							// just read the proper number of bytes so we can all go home
							readBytes(stream,metaLength);
							bytesRead+=metaLength;
						}
						break;
					default:
						throw new IOException("Unrecognized opcode '"+Integer.toHexString(opcode>>4)+"'");
				}
			}
		}
		
		
		List<Track> tracks=new ArrayList<Track>();
		for (int i=0; i<numTracks; i++)
		{
			Track track=new Track(trackNames[i],trackVoices[i],trackNotes.get(i));
			tracks.add(track);
		}
		
		List<Creator> creators=new LinkedList<Creator>();
		creators.add(new Creator(author,"Sequencer"));
		
		SongModel model=new SongModel(tracks,title,creators,email,website,license,bpm);
		return model;
	}
	
	/** Reads a variable-width value from a stream. */
	// side note: variable-width values are reportedly never more than 4 bytes
	// java int = 32-bit, so int should theoretically be fine for storage
	private static int readVariableWidth(InputStream stream) throws IOException
	{
		int value=0;
		int msig;
		int res;
		lastVariableLength=0;
		
		do
		{
			int b=stream.read();
			msig=b & 0x80;
			res=b & 0x7F;
			value=value<<7;
			value+=res;
			lastVariableLength++;
		} while (msig!=0);
		
		return value;
	}
	
	/** Reads an n-byte value from the stream. */
	private static int readBytes(InputStream stream,int num) throws IOException
	{
		int result=0;
		for (int i=0; i<num; i++)
		{
			result=result<<8;
			result+=stream.read();
		}
		return result;
	}
	
	/** Reads a string of length n from the stream. */
	private static String readString(InputStream stream,int n) throws IOException
	{
		StringBuilder res=new StringBuilder();
		for (int i=0; i<n; i++)
		{
			res.append((char)stream.read());
		}
		return res.toString();
	}
}
