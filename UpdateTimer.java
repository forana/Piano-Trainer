package crescendo.base;

public class UpdateTimer implements Runnable{

	private Updatable target;
	private final int FRAMES_PER_SECOND = 300;
	private final double MS_DELAY=1000.0/FRAMES_PER_SECOND;
	/** number of milliseconds from the epoch of when the last frame started */
	private long lastFrame = 0;
	private boolean doContinue;
	private boolean isPaused;
	
	public UpdateTimer(Updatable target){
		super();
		this.target = target;
	}
	
	public void pause(){
		isPaused=true;
	}
	public void resume(){
		isPaused=false;
	}
	public void stop(){
		doContinue=false;
	}
	
	@Override
	public void run() {
		doContinue=true;
		isPaused=false;
		while(doContinue) {
			long now = System.currentTimeMillis();
			if(!isPaused) {
				if(now > (lastFrame + MS_DELAY)) {
					target.update();
					lastFrame = now;	//We want to run at FRAMES_PER_SECOND fps, so use the beginning of the frame to
					//ensure that we get the correct frames, no matter how long update takes
				} else {
					try {
						Thread.sleep(1); // Dont eat up all the processor
					} 
					catch (InterruptedException e) {}
				}
			}else{
				try {
					Thread.sleep(1); // Dont eat up all the processor
				} 
				catch (InterruptedException e) {}
			}
		}
	}

}
