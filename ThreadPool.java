package crescendo.base;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadPool {

	private int threadcount;
	private int threadTimeout;
	private List<Expirator> expirators;
	private SongValidator validator;
	
	public ThreadPool(SongValidator validator,int poolSize,int threadTimeout)
	{
		this.validator = validator;
		this.threadTimeout = threadTimeout;
		threadcount = poolSize;
		expirators = new ArrayList<Expirator>(threadcount);
		Map<Thread,Expirator> threadMap = new HashMap<Thread,Expirator>();
		ExpiratorExceptionHandler eHandler = new ExpiratorExceptionHandler(threadMap);
		for(int i=0;i<threadcount;i++)
		{
			Expirator e = new Expirator(this.threadTimeout);
			Thread t = new Thread(e);
			t.setUncaughtExceptionHandler(eHandler);
			expirators.set(i, e);
			threadMap.put(t, e);
			t.start();
		}
	}

	public Expirator getAvailableExpirator()
	{
		Expirator available = null;
		for(Expirator e : expirators)
		{
			if(!e.isBusy())
			{
				available = e;
				break;
			}
		}
		return available;
	}

	public void pause()
	{
		for(Expirator e : expirators)
		{
			e.pause();
		}
	}

	public void resume()
	{
		for(Expirator e : expirators)
		{
			e.resume();
		}
	}

	public void shutdown()
	{
		for(Expirator e : expirators)
		{
			e.stop();
		}
	}

	private class ExpiratorExceptionHandler implements UncaughtExceptionHandler
	{
		private Map<Thread,Expirator> threadMap;

		public ExpiratorExceptionHandler(Map<Thread,Expirator> threadMap)
		{
			this.threadMap = threadMap;
		}

		@Override
		public void uncaughtException(Thread t, Throwable e) {
			if(e instanceof NoteExpiredException)
			{
				Expirator ex = threadMap.get(t);
				validator.noteExpired(ex.getNoteEvent());
			}
		}
	}
}
