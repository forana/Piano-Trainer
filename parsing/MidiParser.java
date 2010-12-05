package crescendo.base.parsing;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import crescendo.base.ErrorHandler;
import crescendo.base.Note;
import crescendo.base.SongModel;
import crescendo.base.Track;

public class MidiParser implements SongFileParser
{
	// the byte-length of whatever the last variable-length variable was
	private static int lastVariableLength=0;
	
	public SongModel parse(File file)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(file));
			
			// initialize track items - these cannot be track objects yet as they need to be built
			List<List<Note>> trackNotes; // this should REALLY be an array, but java can't do that
			String[] trackNames;
			int[] trackVoices;
			// unfortunately this is the best we can do for these
			String title="Untitled";
			String author=null;
			String email=null;
			String website=null;
			String license=null;
			// bpm defaults to 120
			int bpm=120;
			
			// according to specification at http://www.sonicspot.com/guide/midifiles.html
			// main header
			if (readBytes(br,4)!=0x4D546864) // MThd
			{
				throw new IOException("Missing header identifier");
			}
			if (readBytes(br,4)!=6) // always 6
			{
				throw new IOException("Improper header chunk size");
			}
			int formatType=readBytes(br,2);
			if (formatType!=0 && formatType!=1)
			{
				throw new IOException("MIDI type not accepted in this application");
			}
			int numTracks=readBytes(br,2);
			trackNotes=new ArrayList<List<Note>>(numTracks);
			trackNames=new String[numTracks];
			trackVoices=new int[numTracks];
			for (int i=0; i<numTracks; i++)
			{
				trackNotes.add(new LinkedList<Note>());
				trackNames[i]="";
				trackVoices[i]=0;
			}
			int timeDivisor=readBytes(br,2);
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
				if (readBytes(br,4)!=0x4D54726B) // MTrk
				{
					throw new IOException("Track header not found");
				}
				int trackLength=readBytes(br,4);
				int bytesRead=0;
				while (bytesRead<trackLength)
				{
					int deltaTime=readVariableWidth(br);
					bytesRead+=lastVariableLength;
					int opcode=br.read();
					int channel=opcode & 0x0F;
					bytesRead++;
					switch (opcode>>4)
					{
						case 0x8: // Note off
							int noteOffPitch=br.read();
							int noteOffVelocity=br.read();
							bytesRead+=2;
							break;
						case 0x9: // Note on
							int noteOnPitch=br.read();
							int noteOnVelocity=br.read();
							bytesRead+=2;
							break;
						case 0xA: // Note aftertouch
							int noteAftertouchPitch=br.read();
							int noteAftertouchAmount=br.read();
							bytesRead+=2;
							break;
						case 0xB: // Controller event
							int controllerType=br.read();
							int controllerValue=br.read();
							bytesRead+=2;
							break;
						case 0xC: // Program change
							int programNumber=br.read();
							bytesRead++;
							// set the current track's voice
							trackVoices[i]=programNumber;
							break;
						case 0xD: // Channel aftertouch
							int aftertouchAmount=br.read();
							bytesRead++;
							break;
						case 0xE: // Pitch bend
							int bendLSB=br.read();
							int bendMSB=br.read();
							int bendAmount=bendMSB<<7+bendLSB;
							bytesRead+=2;
							break;
						case 0xF: // Meta event
							if (channel==0xF)
							{
								int metaCode=br.read();
								bytesRead++;
								int metaLength=readVariableWidth(br);
								bytesRead+=lastVariableLength;
								readBytes(br,metaLength);
								bytesRead+=metaLength;
								System.out.println("DAT LENGTH IS "+metaLength+" WID A CODE OF "+Integer.toHexString(metaCode));
								switch (metaCode)
								{
									case 0x00: // Sequence number
										break;
									case 0x01: // Text event
										break;
									case 0x02: // Copyright notice
										break;
									case 0x03: // Sequence / track name
										break;
									case 0x04: // Instrument name
										break;
									case 0x05: // Lyrics
										break;
									case 0x06: // Marker
										break;
									case 0x07: // Cue point
										break;
									case 0x20: // Midi channel prefix
										break;
									case 0x2F: // End of track
										break;
									case 0x51: // Set tempo
										break;
									case 0x54: // SMPTE offset
										break;
									case 0x58: // Time signature
										break;
									case 0x89: // Key signature
										break;
									case 0x7F: // Sequencer-specific event
										break;
									default:
										throw new IOException("Unrecognized meta event code '"+Integer.toHexString(metaCode)+"' @ track "+(i+1)+" offset "+bytesRead);
								}
							}
							else // manufacturer-exclusive event, aka we don't care
							{
								int metaLength=readVariableWidth(br);
								bytesRead+=lastVariableLength;
								// just read the proper number of bytes so we can all go home
								readBytes(br,metaLength);
								bytesRead+=metaLength;
							}
							break;
						default:
							throw new IOException("Unrecognized opcode '"+Integer.toHexString(opcode)+"'");
					}
				}
			}
			
			
			List<Track> tracks=new ArrayList<Track>();
			for (int i=0; i<numTracks; i++)
			{
				Track track=new Track(trackNames[i],trackVoices[i],trackNotes.get(i));
				tracks.add(track);
			}
			
			SongModel model=new SongModel(tracks,title,author,email,website,license,bpm);
			return model;
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			String title="MIDI Format Error";
			String message="The file \""+file+"\" is improperly formatted.";
			ErrorHandler.showNotification(title,message);
			
			return null;
		}
	}
	
	/** Reads a variable-width value from a stream. */
	// side note: variable-width values are reportedly never more than 4 bytes
	// java int = 32-bit, so int should theoretically be fine for storage
	private int readVariableWidth(BufferedReader br) throws IOException
	{
		int value=0;
		int msig;
		int res;
		lastVariableLength=0;
		
		do
		{
			int b=br.read();
			msig=b & 0x80;
			res=b & 0x7F;
			value=value<<7;
			value+=res;
			lastVariableLength++;
		} while (msig!=0);
		
		return value;
	}
	
	/** Reads an n-byte value from the stream. */
	private int readBytes(BufferedReader br,int num) throws IOException
	{
		int result=0;
		for (int i=0; i<num; i++)
		{
			result=result<<8;
			result+=br.read();
		}
		return result;
	}
	
	/** Reads a string of length n from the stream. */
	private String readString(BufferedReader br,int n) throws IOException
	{
		StringBuilder res=new StringBuilder();
		for (int i=0; i<n; i++)
		{
			res.append((char)br.read());
		}
		return res.toString();
	}
}
