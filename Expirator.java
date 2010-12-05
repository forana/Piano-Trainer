package crescendo.base;

public class Expirator implements Runnable{

	private final int TIMEOUT;
	private int currentTimeout;
	private boolean run;
	private boolean busy;
	private boolean paused;
	
	private long milliseconds;
	private NoteEvent currentEvent;
	
	public Expirator(int timeout)
	{
		TIMEOUT = timeout;
		currentTimeout = timeout;
		run = true;
		busy = false;
		milliseconds = 0;
	}
	
	public void expireNote(NoteEvent n)
	{
		currentEvent = n;
		milliseconds = System.currentTimeMillis();
		busy = true;
	}
	
	public NoteEvent getNoteEvent()
	{
		return currentEvent;
	}
	
	public boolean isBusy()
	{
		return busy;
	}
	
	public void pause()
	{
		paused = true;
		long timeExpired = System.currentTimeMillis() - milliseconds;
		if(timeExpired < currentTimeout)
		{
			currentTimeout -= timeExpired;
		}
	}
	
	public void resume()
	{
		milliseconds = System.currentTimeMillis();
		paused = false;
	}

	public void stop()
	{
		run=false;
		this.notify();
	}
	
	@Override
	public void run() {
		while(run)
		{
			if(isBusy())
			{
				
				if(!paused && (System.currentTimeMillis() - milliseconds > currentTimeout))
				{
					busy = false;
					currentTimeout = TIMEOUT;
					currentEvent = null;
					throw new NoteExpiredException();
				}
				else
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO same as below
					}
				}
			}
			else
			{
				try {
					this.wait();
				} catch (InterruptedException e) {
					//TODO research what leads to this case and how to deal with it
				}
			}
		}
	}
}
