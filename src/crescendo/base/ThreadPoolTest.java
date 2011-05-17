package crescendo.base;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import crescendo.base.Expirator.ExpiratorBusyException;
import crescendo.base.song.Note;

public class ThreadPoolTest {

	private Note dummyNote = new Note(60, 4, 1, null); //shortcut for notes
	private long start; //Needed for performing timing compares

	/**
	 * Test the instantiation of the ThreadPool and the allocation of the threads
	 */
	
	@Test
	public void AllocationTest() {
		
		int expectedThreadCount = 2;
		Note dummyNote = new Note(60, 4, 1, null);
		SongValidator dummyValidator = new SongValidator();
		
		List<Expirator> expirators = new ArrayList<Expirator>();
		
		ThreadPool testThreadPool = new ThreadPool(dummyValidator,expectedThreadCount,100);
		Expirator temporaryExpirator;
		
		for(int actualThreadCount=0;actualThreadCount<expectedThreadCount;actualThreadCount++) {
			temporaryExpirator = testThreadPool.getAvailableExpirator();
			if(temporaryExpirator!=null) {
				expirators.add(temporaryExpirator);
				try {
					temporaryExpirator.expireNote(new NoteEvent(dummyNote, NoteAction.BEGIN, System.currentTimeMillis()));
				} catch (ExpiratorBusyException e) {
					fail("Expirator should not be busy");
				}
			}
			else {
				fail("There were fewer threads than expected ("+actualThreadCount+")");
			}
		}
		
		assertTrue(testThreadPool.getAvailableExpirator()==null);
		
		//Tear down nicely so validator doesnt get innundated with noteExpired calls
		for(Expirator e : expirators) {
			e.resolveNote();
		}
	}

	/**
	 * Test the expiration times of the thread pool
	 * @throws ExpiratorBusyException
	 * @throws InterruptedException
	 */
	@Test
	public void ExpireTest() throws ExpiratorBusyException, InterruptedException {
		
		SongValidator validator = new SongValidator(){
			public void noteExpired(NoteEvent event) {
				//Giving the expiration an acceptable error of +/- 3 milliseconds
				//This is due to Thread.sleep (2 should be the worst case scenario)
				assertTrue(
						(System.currentTimeMillis() - start) <103 &&
						(System.currentTimeMillis() - start) > 97
						);
			}
		};
		
		ThreadPool testThreadPool = new ThreadPool(validator,2,100);
		Expirator testExpirator = testThreadPool.getAvailableExpirator();
		start = System.currentTimeMillis(); 
		testExpirator.expireNote(new NoteEvent(dummyNote,NoteAction.BEGIN,System.currentTimeMillis()));
		Thread.sleep(200);//This is so we don't terminate before waiting for the expiration
	}
	
	/**
	 * Test the ability of the pool to shut itself down
	 */
	@Test
	public void ShutdownTest()
	{
		ThreadPool testThreadPool = new ThreadPool(new SongValidator(),2,100);
		testThreadPool.shutdown();
		assertTrue(testThreadPool.getAvailableExpirator()==null);
	}

	/**
	 * Test the correctness of timeouts with a pause involved
	 * @throws ExpiratorBusyException
	 * @throws InterruptedException
	 */
	@Test
	public void PauseTest() throws ExpiratorBusyException, InterruptedException
	{
		SongValidator validator = new SongValidator(){
			public void noteExpired(NoteEvent event) {
				//Giving the expiration an acceptable error of +/- 3 milliseconds
				//This is due to Thread.sleep (2 should be the worst case scenario)
				assertTrue(
						(System.currentTimeMillis() - start) <303 &&
						(System.currentTimeMillis() - start) > 297
						);
			}
		};
		
		ThreadPool testThreadPool = new ThreadPool(validator,2,100);
		Expirator testExpirator = testThreadPool.getAvailableExpirator();
		start = System.currentTimeMillis(); 
		testExpirator.expireNote(new NoteEvent(dummyNote,NoteAction.BEGIN,System.currentTimeMillis()));
		testThreadPool.pause();
		Thread.sleep(200);//This is so we don't terminate before waiting for the expiration
		testThreadPool.resume();
		Thread.sleep(200);
	}
	
	@Test(expected=NullPointerException.class)
	public void NullValidatorTest() {
		ThreadPool testThreadPool = new ThreadPool(null,2,100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void NegativePoolSizeTest() {
		ThreadPool testThreadPool = new ThreadPool(new SongValidator(),-1,100);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void NegativeTimeoutTest() {
		ThreadPool testThreadPool = new ThreadPool(new SongValidator(),2,-1);
	}
	
	@Test
	public void LargePoolSizeTest() {
		ThreadPool testThreadPool = new ThreadPool(new SongValidator(),200,100);
	}
	
}	
