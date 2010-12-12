package crescendo.base.parsing;

import java.io.InputStream;
import java.io.IOException;

/**
 * Provides an InputStream wrapper that provides a few extra methods used in
 * Midi file parsing.
 * @author forana
 */
public class MidiInputStream
{
	private InputStream stream;
	private int lastVariableLength;
	
	public MidiInputStream(InputStream stream)
	{
		this.stream=stream;
		this.lastVariableLength=0;
	}
	
	public int read() throws IOException
	{
		int value=stream.read();
		//System.out.print(Integer.toHexString(value)+" ");
		return value;
	}
	
	public void close() throws IOException
	{
		stream.close();
	}
	
	/** Reads a variable-width value from a stream. */
	// side note: variable-width values are reportedly never more than 4 bytes
	// java int = 32-bit, so int should theoretically be fine for storage
	public int readVariableWidth() throws IOException
	{
		int value=0;
		int msig;
		int res;
		lastVariableLength=0;
		
		do
		{
			int b=this.read();
			msig=b & 0x80;
			res=b & 0x7F;
			value=value<<7;
			value+=res;
			lastVariableLength++;
		} while (msig!=0);
		
		return value;
	}
	
	/** Returns the length of the last read variable-width number in bytes. **/
	public int lastVariableLength()
	{
		return this.lastVariableLength;
	}
	
	/** Reads an n-byte value from the stream. */
	public int readBytes(int num) throws IOException
	{
		int result=0;
		for (int i=0; i<num; i++)
		{
			result=result<<8;
			result+=this.read();
		}
		return result;
	}
	
	/** Reads a string of length n from the stream. */
	public String readString(int n) throws IOException
	{
		StringBuilder res=new StringBuilder();
		for (int i=0; i<n; i++)
		{
			res.append((char)this.read());
		}
		return res.toString();
	}
}
