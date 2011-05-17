package crescendo.base.parsing;

import java.io.InputStream;
import java.io.IOException;
import java.util.Stack;

/**
 * Provides an InputStream wrapper that provides a few extra methods used in Midi file parsing and allows pushing 
 * @author forana
 */
public class MidiInputStream
{
	/** The wrapped stream. */
	private InputStream stream;
	
	/** The length of the last variable-length value that was read, in bytes. */
	private int lastVariableLength;
	
	/** Stack of bytes that have been pushed back onto the stream. */
	private Stack<Integer> pushedBytes;
	
	/**
	 * Creates a new stream, wrapping another stream.
	 * 
	 * @param stream The stream to wrap.
	 */
	public MidiInputStream(InputStream stream)
	{
		this.stream=stream;
		this.lastVariableLength=0;
		this.pushedBytes=new Stack<Integer>();
	}
	
	/**
	 * Reads the next byte from the stream.
	 * 
	 * @return The read byte.
	 * 
	 * @throw IOException if an error occurs.
	 */
	public int read() throws IOException
	{
		int value;
		if (this.pushedBytes.size()>0)
		{
			value=pushedBytes.pop();
		}
		else
		{
			value=stream.read();
		}
		return value;
	}
	
	/**
	 * Close the wrapped stream.
	 * 
	 * @throw IOException if an error occurs.
	 */
	public void close() throws IOException
	{
		stream.close();
	}
	
	/**
	 * Pushes a byte back into the stream.
	 * 
	 * @param data The byte to be pushed back.
	 */
	public void push(int data)
	{
		this.pushedBytes.push(data);
	}
	
	/**
	 * Reads a variable-length value from the stream, as defined by the Midi specification. The length of this value (in bytes)
	 * can be retrieved with lastVariableLength().
	 * 
	 * @return A variable-length value.
	 * 
	 * @throw IOException if an error occurs.
	 */
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
	
	/**
	 * Returns the length of the last read variable-width number in bytes.
	 * 
	 * @return The length of the last read variable-length number, in bytes, or 0 if no variable-length number has yet been read.
	 */
	public int lastVariableLength()
	{
		return this.lastVariableLength;
	}
	
	/**
	 * Reads a specified number of bytes from the stream and shifts them together into a single number.
	 * 
	 * @param num The number of bytes to read.
	 * 
	 * @return The resulting number.
	 * 
	 * @throw IOException if an error occurs.
	 */
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
	
	/**
	 * Reads a string of characters from the stream, without converting character sets.
	 * 
	 * @param n The length of the string.
	 * 
	 * @return The string.
	 * 
	 * @throw IOException if an error occurs.
	 */
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
